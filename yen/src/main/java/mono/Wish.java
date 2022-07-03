package mono;


import mono.http.HttpResponse;

public interface Wish {
    HttpResponse fulfill(Object a);
}

