package server.http;

import java.util.List;

public class HttpMessageParser {

    public HttpMessage parseMessage(String message) {
        List<String> lines = List.of(message.split("\n"));
        if (lines.isEmpty()) {
            throw new IllegalArgumentException(String.format("Http message has 0 lines: %s", message));
        }

        var firstLine = lines.get(0).split(" ");
        if (firstLine.length != 3) {
            throw new IllegalArgumentException(String.format(String.format("Malformed first line of http message: %s", firstLine)));
        }
        HttpMethod method = HttpMethod.valueOf(firstLine[0]);
        String version = firstLine[2];
        //todo add regex to validate url :)
        String url = firstLine[1];

        return new HttpMessage(version, url, method, null, null);
    }

    private String parseBody(String message) {
        return null;
    }

    private List<String> parseHeaders(String message) {
        return null;
    }
}
