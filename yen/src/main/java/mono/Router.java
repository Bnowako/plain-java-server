package mono;

import mono.http.HttpRequest;
import mono.http.HttpResponse;

import java.util.List;
import java.util.Map;

public class Router {

    public HttpResponse route(HttpRequest httpRequest, Map<String, Controller> endpoints) {
        List<String> parts = getPathParts(httpRequest.path());
        Controller controller = endpoints.get(parts.get(1));
        if(controller == null) throw new IllegalArgumentException("Path does not exist");

        String secondSegment = parts.size() < 3 ? "" : parts.get(2);
        Endpoint endpoint = controller.getEndpoint(secondSegment, httpRequest.method());
        if(endpoint == null) throw new IllegalArgumentException("Path does not exist");
        return endpoint.wish().fulfill(httpRequest);
    }

    private List<String> getPathParts(String path) {
        return List.of(path.split("/"));
    }

}
