package mono;

import mono.http.HttpRequest;

import java.util.List;
import java.util.Map;

public class Router {

    public Object route(HttpRequest httpRequest, Map<String, Endpoint> endpoints) {
        List<String> parts = getPathParts(httpRequest.path());
        Endpoint e = endpoints.get(parts.get(1));
        Wish wish = e.wishes().get(parts.get(2));
        if(wish != null) {
            return wish.fulfill(httpRequest);
        } else {
            throw new IllegalArgumentException("Path does not exist");
        }
    }

    private List<String> getPathParts(String path) {
        return List.of(path.split("/"));
    }

}
