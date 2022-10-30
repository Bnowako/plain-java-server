package mono;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Jinn jinn = Jinn.builder()
                .register("users", new UserController())
                .build();

        jinn.makeAWish();
    }
}
