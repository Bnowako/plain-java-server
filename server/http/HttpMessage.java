package server.http;

import java.util.Map;
import java.util.Optional;

public class HttpMessage {
    public final String version;
    public final String url;
    public final HttpMethod method;
    public final Map<String, String> headers;
    public final Optional<String> body;

    public HttpMessage(String version, String url, HttpMethod method, Map<String, String> headers, String body) {
        this.version = version;
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = Optional.ofNullable(body);
    }
}
