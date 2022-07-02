package mono.http;

import java.util.Map;

public class HttpResponse {

    String version;
    HttpStatus status;
    Map<String, String> headers;
    String body;

    public HttpResponse(String version, HttpStatus status, Map<String,String> headers) {
        this.version = version;
        this.status = status;
        this.headers = headers;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(String.format("%s %s %s", this.version, this.status.code, this.status));

        headers.keySet()
                .forEach(k -> {
                    String header = String.format("\n%s: %s", k, this.headers.get(k));
                    s.append(header);
                });

        return s.toString();
    }
}
