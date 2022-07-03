package mono;

import mono.http.HttpRequest;

import java.util.List;
import java.util.Map;

public class Router {

    public Object route(HttpRequest httpRequest, Map<String, Endpoint> endpoints) {
        List<String> parts = getPathParts(httpRequest.path());
        Endpoint e = endpoints.get(parts.get(1));
        if(e == null) throw new IllegalArgumentException("Path does not exist");

        String secondSegment = parts.size() < 3 ? "" : parts.get(2);
        Wish wish = e.getWishesForMethod(httpRequest.method()).get(secondSegment);
        if(wish == null) throw new IllegalArgumentException("Path does not exist");
        return wish.fulfill(httpRequest);
    }

    private List<String> getPathParts(String path) {
        return List.of(path.split("/"));
    }

}
