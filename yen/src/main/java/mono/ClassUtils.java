package mono;

import java.util.Set;

public class ClassUtils {

    private static final Set<Class<?>> types = Set.of(Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE);

    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() || types.contains(clazz);
    }
}
