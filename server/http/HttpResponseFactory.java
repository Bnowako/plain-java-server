package server.http;

public class HttpResponseFactory {

    public static HttpResponse getOkResponse() {
        return new HttpResponse("HTTP/1.1", HttpStatus.OK);
    }
}
