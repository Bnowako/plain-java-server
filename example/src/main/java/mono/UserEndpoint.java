package mono;

import java.util.Map;

class UserEndpoint implements Endpoint {

    Wish createUser = (a) -> {
        return a;
    };

    Wish getUser = (a) -> {
        return a;
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