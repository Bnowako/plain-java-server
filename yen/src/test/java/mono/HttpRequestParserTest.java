package mono;

import mono.http.HttpRequest;
import mono.http.HttpRequestParser;
import mono.http.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static mono.TestUtils.assertEquals;
import static mono.TestUtils.assertNotNull;

@TestClass
public class HttpRequestParserTest {

    String getRequest = """
            GET /lyrics-wizard HTTP/1.1\r
            Host: 127.0.0.1:8080\r
            User-Agent: test-agent\r
            Accept: */*\r""";

    String postRequest = """
            POST /lyrics-wizard HTTP/1.1\r
            Host: 127.0.0.1:8080\r
            User-Agent: test-agent\r
            Accept: */*\r
            \r
            {"name": "quebonafide"}\r""";

    public void test() {
        var methods = List.of(this.getClass().getDeclaredMethods());
        methods.stream().filter(m -> m.isAnnotationPresent(Test.class))
                .forEach(m -> {
                    try {
                        System.out.print(String.format("\nTesting: %s", m.getName()));
                        m.invoke(this);
                        System.out.print(" -> PASSED");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        System.out.printf(" -> %s", e.getTargetException().getMessage());
                    }
                });
    }

    @Test
    public void parseMethod() {
        var parser = new HttpRequestParser();
        HttpRequest parsedMessage =  parser.parse(getRequest.getBytes(StandardCharsets.UTF_8), getRequest.length());
        assertNotNull(parsedMessage);
        assertEquals(parsedMessage.method, HttpMethod.GET);
    }

    @Test
    public void parseUrl() {
        var parser = new HttpRequestParser();
        HttpRequest parsedMessage =  parser.parse(getRequest.getBytes(StandardCharsets.UTF_8), getRequest.length());
        assertNotNull(parsedMessage);
        assertEquals(parsedMessage.path, "/lyrics-wizard");
    }

    @Test
    public void parseVersion() {
        var parser = new HttpRequestParser();
        HttpRequest parsedMessage = parser.parse(getRequest.getBytes(StandardCharsets.UTF_8), getRequest.length());
        assertNotNull(parsedMessage);
        assertEquals(parsedMessage.version, "HTTP/1.1");
    }

    @Test
    public void parseHeader() {
        var parser = new HttpRequestParser();
        HttpRequest parsedMessage = parser.parse(getRequest.getBytes(StandardCharsets.UTF_8), getRequest.length());
        assertNotNull(parsedMessage);
        assertEquals(parsedMessage.headers.get("Host"), "127.0.0.1:8080");
        assertEquals(parsedMessage.headers.get("User-Agent"), "test-agent");
        assertEquals(parsedMessage.headers.get("Accept"), "*/*");
    }

    @Test
    public void parseBody() {
        var parser = new HttpRequestParser();
        HttpRequest parsedMessage = parser.parse(postRequest.getBytes(StandardCharsets.UTF_8), postRequest.length());
        assertNotNull(parsedMessage);
        assertEquals(parsedMessage.body.orElse(""), "{\"name\": \"quebonafide\"}");
    }
}
