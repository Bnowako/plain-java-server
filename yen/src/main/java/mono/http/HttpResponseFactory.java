package mono.http;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseFactory {

    public static HttpResponse getOkResponse() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Date", LocalDateTime.now(ZoneOffset.UTC).toString());
        return new HttpResponse("HTTP/1.1", HttpStatus.OK, headers);
    }

    public static HttpResponse getOkResponse(String body) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Date", LocalDateTime.now(ZoneOffset.UTC).toString());
        return new HttpResponse("HTTP/1.1", HttpStatus.OK, headers, body);
    }
}
