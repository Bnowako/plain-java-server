package mono;

import mono.http.HttpMethod;
import mono.http.HttpResponseFactory;
import mono.http.HttpStatus;

import java.util.List;
import java.util.Map;

class UserController implements Controller {

    Wish createUser = (a) -> {
        return HttpResponseFactory.createResponse("{\"id\": \"123\"}", HttpStatus.CREATED);
    };

    Wish getUser = (a) -> {
        return HttpResponseFactory.createResponse("{\"user\": \"dummy\"}", HttpStatus.OK);
    };

    @Override
    public List<Endpoint> endpoints() {
        return List.of(
                new Endpoint("", HttpMethod.GET, getUser),
                new Endpoint("", HttpMethod.POST, createUser)
        );
    }

}