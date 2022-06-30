package server.test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class TestRunner {

    public void allTests() {
        this.getClass().getClassLoader();

    }


    public void runClass(String className) {

//        var methods = List.of(this.getClass().getClassLoader().getDeclaredMethods());
//        methods.stream().filter(m -> m.isAnnotationPresent(Test.class))
//                .forEach(m -> {
//                    try {
//                        System.out.print(String.format("\nTesting: %s", m.getName()));
//                        m.invoke(this);
//                        System.out.print(" -> PASSED");
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        System.out.printf(" -> %s", e.getTargetException().getMessage());
//                    }
//                });
    }
}
