package server.http;

import java.util.Map;
import java.util.Optional;

public class HttpMessage {
    final String version;
    final String url;
    final HttpMethod method;
    final Map<String, String> headers;
    final Optional<String> body;

    public HttpMessage(String version, String url, HttpMethod method, Map<String, String> headers, String body) {
        this.version = version;
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = Optional.ofNullable(body);
    }
}
