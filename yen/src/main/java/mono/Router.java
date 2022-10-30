package mono;

import mono.http.HttpRequest;
import mono.http.HttpResponse;

import java.util.List;
import java.util.Map;

public class Router {
    private final KlaksonDeser deser = new KlaksonDeser();

    public HttpResponse route(HttpRequest httpRequest, Map<String, Controller> endpoints) {
        List<String> parts = getPathParts(httpRequest.path());
        Controller controller = endpoints.get(parts.get(1));
        if(controller == null) throw new IllegalArgumentException("Path does not exist");

        String secondSegment = parts.size() < 3 ? "" : parts.get(2);
        Endpoint endpoint = controller.getEndpoint(secondSegment, httpRequest.method());
        if(endpoint == null) throw new IllegalArgumentException("Path does not exist");

        Object deserBody = null;
        if(endpoint.bodyClass() != null) {
            if(httpRequest.body().isEmpty()) throw new IllegalArgumentException("Body must be present");
            deserBody = deser.deser(httpRequest.body().get(), endpoint.bodyClass());
            System.out.printf("Deser done! body: %s%n", deserBody.toString());
        }

        return endpoint.wish().fulfill(httpRequest, deserBody);
    }

    private List<String> getPathParts(String path) {
        return List.of(path.split("/"));
    }

}
