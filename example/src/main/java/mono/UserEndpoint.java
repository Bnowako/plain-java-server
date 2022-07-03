package mono;

import mono.http.HttpResponseFactory;
import mono.http.HttpStatus;

import java.util.Map;

class UserEndpoint implements Endpoint {

    Wish createUser = (a) -> {
        return HttpResponseFactory.createResponse("{\"id\": \"123\"}", HttpStatus.CREATED);
    };

    Wish getUser = (a) -> {
        return HttpResponseFactory.createResponse("{\"user\": \"dummy\"}", HttpStatus.OK);
    };

    @Override
    public Map<String, Wish> getWishes() {
        return Map.of("", getUser);
    }

    @Override
    public Map<String, Wish> postWishes() {
        return Map.of(
                "", createUser
        );
    }
}