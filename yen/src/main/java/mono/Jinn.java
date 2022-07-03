package mono;

import mono.http.HttpRequestParser;
import mono.http.HttpResponseFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Jinn {
    private final Map<String, Endpoint> endpoints;
    private final HttpRequestParser httpRequestParser = new HttpRequestParser();

    private Jinn(Map<String, Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public static Jinn.Builder builder() {
        return new Jinn.Builder();
    }

    public void makeAWish() throws IOException {
        ServerSocket socket = initSocket();

        while (true) {
            var s = socket.accept();
            System.out.printf("Socket accepted client at %s\n", s.getPort());

            new Thread(() -> processRequest(s)).start();
        }
    }

    private void processRequest(Socket s) {
        try {
            System.out.printf("Trying to read on new socket - thread: %s\n", Thread.currentThread().getName());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(s.getInputStream());
            byte[] bytes = new byte[4096];
            var bytesCount = bufferedInputStream.read(bytes);
            var request = httpRequestParser.parse(bytes, bytesCount);
            System.out.printf("Read message:\n%s\n on socket %s\n", request.version, s.getPort());

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(s.getOutputStream());
            bufferedOutputStream.write(HttpResponseFactory.getOkResponse().toString().getBytes());
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
        Map<String, Endpoint> endpoints = new HashMap<>();

        public Jinn.Builder register(String basePath, Endpoint endpoint) {
            endpoints.put(basePath, endpoint);
            return this;
        }

        public Jinn build() {
            return new Jinn(endpoints);
        }
    }
}
