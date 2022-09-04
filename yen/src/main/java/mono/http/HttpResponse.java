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
        StringBuilder s = new StringBuilder(String.format("%s %s %s", this.version, this.status.code, this.status));
        if(this.body != null) {
            s.append(this.body);
        }
        return s.toString();
    }
}
