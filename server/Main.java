package server;

import server.http.HttpResponseFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = initSocket();

        while (true) {
            var s = socket.accept();
            System.out.printf("Socket accepted client at %s\n", s.getPort());
            new Thread(() -> processClient(s)).start();
        }
    }

    private static void processClient(Socket s) {
        try {
            System.out.printf("Trying to read on new socket - thread: %s\n", Thread.currentThread().getName());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(s.getInputStream());
            byte[] bytes = new byte[4096];
            var bytesCount = bufferedInputStream.read(bytes);
            var message = new String(Arrays.copyOfRange(bytes, 0, bytesCount));
            System.out.printf("Read message:\n%s\n on socket %s\n", message, s.getPort());

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(s.getOutputStream());
            bufferedOutputStream.write(HttpResponseFactory.getOkResponse().toString().getBytes());
            bufferedOutputStream.flush();

            s.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static ServerSocket initSocket() throws IOException {
        var socket = new ServerSocket();
        socket.bind(new InetSocketAddress(8080));
        return socket;
    }
}
