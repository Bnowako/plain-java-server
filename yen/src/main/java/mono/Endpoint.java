package mono;

import mono.http.HttpMethod;

 public record Endpoint(String path, HttpMethod httpMethod, Wish wish, Class<?> bodyClass) {
    public Endpoint(String path, HttpMethod httpMethod, Wish wish) {
        this(path, httpMethod, wish, null);
    }
}
