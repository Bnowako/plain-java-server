package server.test;

public class TestUtils {
    public static void assertNotNull(Object object) {
        assert object != null : "Actual null";
    }

    public static void assertEquals(Object expected, Object actual) {
        assert expected.equals(actual) : String.format("Expected: %s, Actual: %s", expected.toString(), actual.toString());
    }
}
