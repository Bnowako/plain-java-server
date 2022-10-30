package mono;


import mono.http.HttpRequest;
import mono.http.HttpResponse;

public interface Wish {
    HttpResponse fulfill(HttpRequest httpRequest, Object deserBody);
}

