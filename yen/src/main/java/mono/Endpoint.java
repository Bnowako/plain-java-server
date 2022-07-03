package mono;

import mono.http.HttpMethod;

import java.util.Map;

interface Endpoint {
    default Map<String, Wish> getWishes() {
        return Map.of();
    };
    default Map<String, Wish> postWishes() {
        return Map.of();
    };
    default Map<String, Wish> deleteWishes() {
        return Map.of();
    };
    default Map<String, Wish> putWishes() {
        return Map.of();
    };

    default Map<String, Wish> getWishesForMethod(HttpMethod httpMethod) {
        return switch (httpMethod) {
            case GET -> this.getWishes();
            case POST -> this.postWishes();
            case PUT -> this.putWishes();
            case DELETE -> this.deleteWishes();
            default -> throw new IllegalArgumentException("Not implemented yet");
        };
    }
}
