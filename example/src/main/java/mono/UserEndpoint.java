package mono;

import java.util.Map;

class UserEndpoint implements Endpoint {

    Wish createUser = (a) -> {
        return a;
    };

    @Override
    public Map<String, Wish> wishes() {
        return Map.of(
                "find", createUser
        );
    }
}