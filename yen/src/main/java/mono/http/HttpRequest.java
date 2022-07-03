package mono.http;

import java.util.Map;
import java.util.Optional;

public record HttpRequest(String version,
                          String path,
                          HttpMethod method,
                          Map<String, String> headers,
                          Optional<String> body) {
}
