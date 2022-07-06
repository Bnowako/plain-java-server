package mono.klakson;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public interface Deser {
    <T> T deser(String json, Class<T> clazz);

    String ser(Object object);
}

class KlaksonDeser implements Deser {
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
        START, BEFORE_FIELD_NAME, FIELD_NAME_START, FIELD_NAME_END, SEMICOLON, FIELD_START, FIELD_END;
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
            // opening bracket
            if (state == ParserState.START && c == '{') {
                state = ParserState.BEFORE_FIELD_NAME;
                continue;
            }

            // start of field name
            if (state == ParserState.BEFORE_FIELD_NAME) {
                if (c == '"') {
                    state = ParserState.FIELD_NAME_START;
                } else {
                    // whitespace
                    continue;
                }
                continue;
            }

            // field name
            if (state == ParserState.FIELD_NAME_START) {
                if (c != '"') { //build field name
                    sb.append(c);
                } else { // field name end
                    currentFieldName = sb.toString();
                    currentFieldType = fieldsByType.get(currentFieldName);
                    sb.setLength(0);
                    state = ParserState.FIELD_NAME_END;
                }
                continue;
            }

            // semi colon
            if (state == ParserState.FIELD_NAME_END) {
                if (c == ':') {
                    state = ParserState.SEMICOLON;
                } else {
                    throw new IllegalStateException("Semicolon expected after field name");
                }
                continue;
            }

            if (state == ParserState.SEMICOLON) {
                if (c == ' ') {
                    continue;
                } else {
                    sb.append(c);
                    state = ParserState.FIELD_START;
                }
                continue;
            }

            if (state == ParserState.FIELD_START) {
                if (c == ',' || c == '\n') { // TODO: support for string
                    //field value end
                    state = ParserState.BEFORE_FIELD_NAME;
                    String fieldValue = sb.toString();
                    var res = parseValue(fieldValue, currentFieldType);
                    result.put(currentFieldName, res);
                    sb.setLength(0);
                } else {
                    //build field value
                    sb.append(c);
                }
            }
        }
        return result;
    }

    private Object parseValue(String rawFieldValue, Class<?> fieldType) {
        if (fieldType == Integer.TYPE) {
            return Integer.valueOf(rawFieldValue);
        } else {
            throw new IllegalStateException("Unsupported field type");
        }
    }
}
