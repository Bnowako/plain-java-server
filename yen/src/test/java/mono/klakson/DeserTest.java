package mono.klakson;

import mono.Test;
import mono.TestClass;

import java.util.Objects;

record Inter(int x, int y) {
}

@TestClass
public class DeserTest {

    Deser deser = new KlaksonDeser();

    @Test
    public void deserializeInt() {
        var json = """
                {
                  "x": 10,
                  "y": -2
                }
                """;

        Inter result = deser.deser(json, Inter.class);

        assert Objects.equals(result, new Inter(10, -2));
    }
}
