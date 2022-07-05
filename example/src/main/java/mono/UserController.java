package mono;

import mono.http.HttpMethod;
import mono.http.HttpResponseFactory;
import mono.http.HttpStatus;

import java.util.List;

class UserController implements Controller {

    Wish createUser = (httpRequest) -> HttpResponseFactory.createResponse("{\"id\": \"123\"}", HttpStatus.CREATED);

    Wish getUser = (httpRequest) -> HttpResponseFactory.createResponse("{\"user\": \"dummy\"}", HttpStatus.OK);

    @Override
    public List<Endpoint> endpoints() {
        return List.of(
                new Endpoint("", HttpMethod.GET, getUser),
                new Endpoint("", HttpMethod.POST, createUser)
        );
    }

}