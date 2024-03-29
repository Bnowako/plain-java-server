package mono.http;

public enum HttpStatus {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    CREATED(201),
    SERVER_ERROR(500);

    public final int code;

    HttpStatus(int code) {
        this.code = code;
    }

}
