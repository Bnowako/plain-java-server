package mono;

import mono.http.HttpMethod;

import java.util.List;

interface Controller {
    List<Endpoint> endpoints();

    default Endpoint getEndpoint(String path, HttpMethod method) {
        return endpoints()
                .stream()
                .filter(endpoint -> endpoint.path().equalsIgnoreCase(path) && endpoint.httpMethod() == method)
                .findFirst()
                .orElse(null);
    }
}
