package mono;

import java.util.List;

class UserEndpoint implements Endpoint {

    Wish createUser = (a) -> {
        return a;
    };

    @Override
    public List<Wish> wishes() {
        return List.of(
                createUser
        );
    }
}