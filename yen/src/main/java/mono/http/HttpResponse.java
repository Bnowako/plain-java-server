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
    public HttpResponse(String version, HttpStatus status, Map<String,String> headers, String body) {
        this.version = version;
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(String.format("%s %s %s\n", this.version, this.status.code, this.status));
        headers.keySet()
                .forEach(k -> {
                    String header = String.format("%s: %s\r\n", k, this.headers.get(k));
                    s.append(header);
                });
        s.append("\r\n");
        if(this.body != null) {
            s.append(this.body);
        }
        return s.toString();
    }
}
