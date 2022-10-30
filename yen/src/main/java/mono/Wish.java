package mono;


import mono.http.HttpRequest;

public interface Wish {
    Object fulfill(HttpRequest httpRequest, Object deserBody);
}

