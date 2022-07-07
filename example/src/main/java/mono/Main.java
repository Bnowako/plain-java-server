package mono;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Deser deser = new KlaksonDeser();

        Jinn jinn = Jinn.builder()
                .register("users", new UserController(deser))
                .build();

        jinn.makeAWish();
    }
}
