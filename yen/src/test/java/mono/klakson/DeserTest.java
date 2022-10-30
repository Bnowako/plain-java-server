package mono.klakson;

import mono.Deser;
import mono.KlaksonDeser;
import mono.Test;
import mono.TestClass;
import mono.klakson.common.*;

import static mono.TestUtils.assertEquals;

@TestClass
public class DeserTest {

    Deser deser = new KlaksonDeser();

    @Test
    public void deserializeDoubleNested() {
        var json = """
                {
                  "x": 10,
                  "nested": {
                    "x": "hello",
                    "nested": {
                        "x": "hello",
                        "y": "world"
                        }
                   }
                }
                """;

        DoubleNestedRecord result = deser.deser(json, DoubleNestedRecord.class);
        assertEquals(result, new DoubleNestedRecord(10, new NestedRecord("hello", new StringRecord("hello", "world"))));
    }

    @Test
    public void deserializeNested() {
        var json = """
                {
                  "x": "hello world",
                  "nested": {
                    "x": "hello",
                    "y": "world"
                    }
                }
                """;

        NestedRecord result = deser.deser(json, NestedRecord.class);
        assertEquals(result, new NestedRecord("hello world", new StringRecord("hello", "world")));
    }

    @Test
    public void deserializeInt() {
        var json = """
                {
                  "x": 10,
                  "y": -2
                }
                """;

        IntRecord result = deser.deser(json, IntRecord.class);

        assertEquals(result, new IntRecord(10, -2));
    }

    @Test
    public void deserializeBoolean() {
        var json = """
                {
                  "x": true,
                  "y": false
                }
                """;

        BooleanRecord result = deser.deser(json, BooleanRecord.class);

        assertEquals(result, new BooleanRecord(true, false));
    }

    @Test
    public void deserializeLong() {
        var json = """
                {
                  "x": 32040202,
                  "y": -83274292
                }
                """;

        LongRecord result = deser.deser(json, LongRecord.class);

        assertEquals(result, new LongRecord(32040202L, -83274292L));
    }

    @Test
    public void deserializeDouble() {
        var json = """
                {
                  "x": 32.01,
                  "y": -16.09
                }
                """;

        DoubleRecord result = deser.deser(json, DoubleRecord.class);

        assertEquals(result, new DoubleRecord(32.01d, -16.09d));
    }

    @Test
    public void deserializeFloat() {
        var json = """
                {
                  "x": 32.01,
                  "y": -16.09
                }
                """;

        FloatRecord result = deser.deser(json, FloatRecord.class);

        assertEquals(result, new FloatRecord(32.01f, -16.09f));
    }

    @Test
    public void deserializeString() {
        var json = """
                {
                  "x": "ala ma kota",
                  "y": "zbigniew ma psa \n \\" i kota"
                }
                """;

        StringRecord result = deser.deser(json, StringRecord.class);

        assertEquals(result, new StringRecord("ala ma kota", "zbigniew ma psa \n \\\" i kota"));
    }

    @Test
    public void deserializeMixed() {
        var json = """
                {
                  "i": 10,
                  "b": true,
                  "l": 2450,
                  "d": 0.1,
                  "f": 0.2,
                  "s": "hello world"
                }
                """;

        MixedRecord result = deser.deser(json, MixedRecord.class);

        assertEquals(result, new MixedRecord(10, true, 2450L, 0.1d, 0.2f, "hello world"));
    }

    @Test
    public void deserializeNonPublicNestedClass() throws NoSuchFieldException {
        var json = """
                {
                  "name": "Pavetta",
                  "age": 22,
                  "address": {
                   "city": "Cintra",
                   "street": "Rynkowa"
                  }
                }
                """;

        User result = deser.deser(json, User.class);

        assertEquals(result, User.withAddress("Pavetta", 22, "Cintra", "Rynkowa"));
    }


}
