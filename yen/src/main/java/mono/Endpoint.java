package mono;

import mono.http.HttpMethod;

public record Endpoint(String path, HttpMethod httpMethod, Wish wish) {}
