package mono;

public interface Deser {
    <T> T deser(String json, Class<T> clazz);

    String ser(Object object);
}

