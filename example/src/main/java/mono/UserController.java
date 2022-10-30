package mono;

import mono.http.HttpMethod;

import java.util.List;

class UserController implements Controller {

    Wish createUser() {
        return (httpRequest, deserBody) -> deserBody;
    }

    Wish getUser() {
        return (httpRequest, deserBody) -> new User("Henry", 303, new Address("już nie rivia", "costam"));
    }

    @Override
    public List<Endpoint> endpoints() {
        return List.of(
                new Endpoint("", HttpMethod.GET, getUser()),
                new Endpoint("", HttpMethod.POST, createUser(), User.class)
        );
    }

}
