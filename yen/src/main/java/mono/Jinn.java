package mono;

import mono.http.HttpRequestParser;
import mono.http.HttpResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Jinn {
    private final Map<String, Controller> endpoints;
    private final HttpRequestParser httpRequestParser = new HttpRequestParser();
    private final Router router = new Router();

    private Jinn(Map<String, Controller> endpoints) {
        this.endpoints = endpoints;
    }

    public static Jinn.Builder builder() {
        return new Jinn.Builder();
    }

    public void makeAWish() throws IOException {
        ServerSocket socket = initSocket();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        while (true) {
            var s = socket.accept();
            System.out.printf("Socket accepted client at %s\n", s.getPort());
            executorService.submit(() -> processRequest(s));
        }
    }

    private void processRequest(Socket s) {
        try {
            System.out.printf("Trying to read on new socket - thread: %s\n", Thread.currentThread().getName());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(s.getInputStream());
            byte[] bytes = new byte[4096];
            var bytesCount = bufferedInputStream.read(bytes);
            var request = httpRequestParser.parse(bytes, bytesCount);
            System.out.printf("Read message:\n%s\n on socket %s\n", request.version(), s.getPort());

            HttpResponse response = router.route(request, endpoints);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(s.getOutputStream());
            bufferedOutputStream.write(response.toString().getBytes());
            bufferedOutputStream.flush();

            s.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private ServerSocket initSocket() throws IOException {
        var socket = new ServerSocket();
        socket.bind(new InetSocketAddress(8080));
        return socket;
    }

    public static class Builder {
        Map<String, Controller> endpoints = new HashMap<>();

        public Jinn.Builder register(String basePath, Controller endpoint) {
            endpoints.put(basePath, endpoint);
            return this;
        }

        public Jinn build() {
            return new Jinn(endpoints);
        }
    }
}
