package mono;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Jinn jinn = Jinn.builder()
                .register("user", new UserEndpoint())
                .build();

        jinn.makeAWish();
    }
}
