package mono;

import java.util.Objects;

public class TestUtils {
    public static void assertNotNull(Object object) {
        assert object != null : "Actual null";
    }

    public static void assertEquals(Object expected, Object actual) {
        assert !Objects.isNull(expected) : "FAILED Expected: is null";
        assert !Objects.isNull(actual) : "FAILED Actual: is null";
        assert expected.equals(actual) : String.format("Expected: %s, Actual: %s", expected.toString(), actual.toString());
    }

    public static void assertThrows(ExecMe execMe, Class<?> exceptionClass) {
        try {
            execMe.execute();
        } catch (Throwable e) {
            assert e.getClass().equals(exceptionClass) : "FAILED Function thrown another exception: %s".formatted(e.getClass().toString());
            return;
        }
        assert false : "Function did not throw %s".formatted(exceptionClass.toString());
    }
}

