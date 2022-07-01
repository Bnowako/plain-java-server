package mono.http;

public class HttpResponse {

    String version;
    HttpStatus status;

    public HttpResponse(String version, HttpStatus status) {
        this.version = version;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", this.version, this.status.code, this.status);
    }
}
