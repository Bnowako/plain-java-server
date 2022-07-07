package mono;

import mono.http.HttpMethod;
import mono.http.HttpResponseFactory;
import mono.http.HttpStatus;

import java.util.List;

class UserController implements Controller {

    private final Deser deser;

    public UserController(Deser deser) {
        this.deser = deser;
    }

    Wish createUser() {
        return (httpRequest) -> {
            if (httpRequest.body().isEmpty()) throw new IllegalArgumentException("Body must be present to create user");
            User user = deser.deser(httpRequest.body().get(), User.class);
            System.out.printf("Create user: %s%n", user.toString());
            return HttpResponseFactory.createResponse("{\"id\": \"123\"}", HttpStatus.CREATED);
        };
    }

    Wish getUser() {
        return (httpRequest) -> HttpResponseFactory.createResponse("{\"user\": \"dummy\"}", HttpStatus.OK);
    }

    @Override
    public List<Endpoint> endpoints() {
        return List.of(
                new Endpoint("", HttpMethod.GET, getUser()),
                new Endpoint("", HttpMethod.POST, createUser())
        );
    }

}