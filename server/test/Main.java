package server.test;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        test();
    }

    public static void runClass(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object o = clazz.getDeclaredConstructor().newInstance();
        var methods = List.of(clazz.getDeclaredMethods());
        methods.stream().filter(m -> m.isAnnotationPresent(Test.class))
                .forEach(m -> {
                    try {
                        System.out.print(String.format("\nTesting: %s", m.getName()));
                        m.invoke(o);
                        System.out.print(" -> PASSED");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        System.out.printf(" -> %s", e.getTargetException().getMessage());
                    }
                });
    }

    private static void test() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Set<String> paths = new HashSet<>();
        getTestClasses(Path.of("./server/test"), paths);
        paths.forEach(System.out::println);

        for (String classPath : paths) {
            try {
                Class<?> clazz = Class.forName(classPath);
                if (clazz.isAnnotationPresent(TestClass.class)) {
                    runClass(clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


    private static void getTestClasses(Path rootPath, Set<String> paths) {

        try {
            List<Path> pathsList = Files.list(rootPath).collect(Collectors.toList());
            pathsList.forEach(p -> {
                if (Files.isDirectory(p)) {
                    getTestClasses(p, paths);
                } else if (p.getFileName().toString().endsWith(".java")) {
                    var cleanedPath = p.toString().replace(".java", "").replace("./", "").replace("/", ".");
                    paths.add(cleanedPath);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
