package mono.http;

import java.util.*;

public class HttpRequestParser {

    public HttpRequest parse(byte[] bytes, int bytesCount) {
        var message = new String(Arrays.copyOfRange(bytes, 0, bytesCount));
        List<String> lines = List.of(message.split("\n"));
        if (lines.isEmpty()) {
            throw new IllegalArgumentException(String.format("Http message has 0 lines: %s", message));
        }
        return parseLines(lines);
    }

    private HttpRequest parseLines(List<String> lines) {
        Map<String, String> headers = new HashMap<>();
        FirstLine firstLine = null;
        Optional<String> body = Optional.empty();

        boolean headersParsed = false;
        for (int i = 0; i < lines.size(); i++) {
            String currLine = lines.get(i);
            if (!headersParsed && currLine.equals("")) {
                headersParsed = true;
                continue;
            }
            if (i == 0) {
                firstLine = parseFirstLine(lines.get(0));
            } else if (!headersParsed) {
                Map.Entry<String, String> stringStringEntry = parseHeader(currLine);
                headers.put(stringStringEntry.getKey(), stringStringEntry.getValue());
            } else {
                body = Optional.of(parseBody(lines.subList(i, lines.size())));
            }
        }
        if(firstLine == null) {
            throw new IllegalArgumentException("Empty first line");
        }

        return new HttpRequest(firstLine.version, firstLine.path, firstLine.method, headers, body);
    }

    private FirstLine parseFirstLine(String firstLine) {
        var lineElements = firstLine.split(" ");
        if (lineElements.length != 3) {
            throw new IllegalArgumentException(String.format(String.format("Malformed first line of http message: %s", lineElements)));
        }
        HttpMethod method = HttpMethod.valueOf(lineElements[0]);
        String path = lineElements[1];
        String version = lineElements[2];
        return new FirstLine(method, path, version);
    }

    private String parseBody(List<String> lines ) {
        return lines.stream().reduce((a,b) -> a + b).orElse("");
    }

    private Map.Entry<String, String> parseHeader(String line) {
        return Map.entry("a", "a");
    }

    record FirstLine(HttpMethod method, String path, String version) {
    }

}
