package mono;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class KlaksonDeser implements Deser {
    @Override
    public <T> T deser(String json, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new IllegalArgumentException("Class under deserialization must contain only one constructor");
        }

        Map<String, Object> fieldValues = parseString(json, fields);

        Constructor<T> constructor = (Constructor<T>) constructors[0];
        Parameter[] parameters = constructor.getParameters();
        Object[] constructorParams = Arrays.stream(parameters).map(x -> fieldValues.get(x.getName())).toArray();

        try {
            return constructor.newInstance(constructorParams);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String ser(Object object) {
        return null;
    }

    enum ParserState {
        START, BEFORE_FIELD_NAME, FIELD_NAME, AFTER_FIELD_NAME, BEFORE_FIELD_VALUE, FIELD_VALUE;
    }

    private Map<String, Object> parseString(String json, Field[] fields) {
        Map<String, Class<?>> fieldsByType = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, Field::getType));
        ParserState state = ParserState.START;
        StringBuilder sb = new StringBuilder();
        String currentFieldName = null;
        Class<?> currentFieldType = null;
        Map<String, Object> result = new HashMap<>();


        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (state == ParserState.START) {
                if (c == '{') {
                    state = ParserState.BEFORE_FIELD_NAME;
                } else {
                    throw new IllegalArgumentException(String.format("Json object must start with a '{' but got %s", c));
                }
                continue;
            }

            if (state == ParserState.BEFORE_FIELD_NAME) {
                if (c == '"') {
                    state = ParserState.FIELD_NAME;
                }
                continue;
            }

            if (state == ParserState.FIELD_NAME) {
                if (c != '"') {
                    sb.append(c);
                } else {
                    currentFieldName = sb.toString();
                    currentFieldType = fieldsByType.get(currentFieldName);
                    sb.setLength(0);
                    state = ParserState.AFTER_FIELD_NAME;
                }
                continue;
            }

            if (state == ParserState.AFTER_FIELD_NAME) {
                if (c == ':') {
                    state = ParserState.BEFORE_FIELD_VALUE;
                }
                continue;
            }

            if (state == ParserState.BEFORE_FIELD_VALUE) {
                if (c != ' ') {
                    state = ParserState.FIELD_VALUE;
                    if (currentFieldType == String.class) {
                        if (c != '"') throw new IllegalArgumentException("String must begin with \"");
                    } else {
                        sb.append(c);
                    }
                }
                continue;
            }

            if (state == ParserState.FIELD_VALUE) {
                if (currentFieldType == String.class) {
                    if (c == '"' && json.charAt(i - 1) != '\\') {
                        state = ParserState.BEFORE_FIELD_NAME;
                        String value = sb.toString();
                        result.put(currentFieldName, value);
                        sb.setLength(0);
                    } else {
                        sb.append(c);
                    }
                } else {
                    if (c == ',' || c == '\n' || c == '}') {
                        state = ParserState.BEFORE_FIELD_NAME;
                        Object value = parseValue(sb.toString(), currentFieldType);
                        result.put(currentFieldName, value);
                        sb.setLength(0);
                    } else {
                        sb.append(c);
                    }
                }
                continue;
            }


        }
        return result;
    }


    private Object parseValue(String rawFieldValue, Class<?> fieldType) {
        if (fieldType == Integer.TYPE) {
            return Integer.valueOf(rawFieldValue);
        } else if (fieldType == Boolean.TYPE) {
            return Boolean.valueOf(rawFieldValue);
        } else if (fieldType == Long.TYPE) {
            return Long.valueOf(rawFieldValue);
        } else if (fieldType == Float.TYPE) {
            return Float.valueOf(rawFieldValue);
        } else if (fieldType == Double.TYPE) {
            return Double.valueOf(rawFieldValue);
        } else {
            throw new IllegalStateException("Unsupported field type");
        }
    }
}
