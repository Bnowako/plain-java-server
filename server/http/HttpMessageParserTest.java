package server.http;

import server.test.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static server.test.TestUtils.assertEquals;
import static server.test.TestUtils.assertNotNull;


public class HttpMessageParserTest {

    String exampleMessage = "GET /lyrics-wizard HTTP/1.1\n" +
            "Host: 127.0.0.1:8080\n" +
            "User-Agent: curl/7.79.1\n" +
            "Accept: */*";

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
        var parser = new HttpMessageParser();
        HttpMessage parsedMessage =  parser.parseMessage(exampleMessage);
        assertNotNull(parsedMessage);
        assertEquals(parsedMessage.method, HttpMethod.GET);
    }

    @Test
    public void parseUrl() {
        var parser = new HttpMessageParser();
        HttpMessage parsedMessage =  parser.parseMessage(exampleMessage);
        assertNotNull(parsedMessage);
        assertEquals(parsedMessage.url, "/lyrics-wizard");
    }

    @Test
    public void parseVersion() {
        var parser = new HttpMessageParser();
        HttpMessage parsedMessage = parser.parseMessage(exampleMessage);
        assertNotNull(parsedMessage);
        assertEquals(parsedMessage.version, "HTTP/1.1");
    }
}
