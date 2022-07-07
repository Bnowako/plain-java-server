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
        return deserialize(json, clazz, 0,0).obj;
    }

    @Override
    public String ser(Object object) {
        return null;
    }

    private <T> DeserResult<T> deserialize(String json, Class<T> clazz, int start, int level) {
        Field[] fields = clazz.getDeclaredFields();
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new IllegalArgumentException("Class under deserialization must contain only one constructor");
        }

        var fieldsDeserializationResult = getFields(json, fields, start, level);
        var fieldValuesByName = fieldsDeserializationResult.fields;

        Constructor<T> constructor = (Constructor<T>) constructors[0];
        Parameter[] parameters = constructor.getParameters();
        Object[] constructorParams = Arrays.stream(parameters).map(x -> fieldValuesByName.get(x.getName())).toArray();

        try {
            return new DeserResult<>(fieldsDeserializationResult.charsRead, constructor.newInstance(constructorParams));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    enum ParserState {
        START, BEFORE_FIELD_NAME, FIELD_NAME, AFTER_FIELD_NAME, BEFORE_FIELD_VALUE, FIELD_VALUE;
    }

    private FieldDeserialization getFields(String json, Field[] fields, int start, int level) {
        Map<String, Class<?>> fieldsByType = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, Field::getType));
        ParserState state = ParserState.START;
        StringBuilder sb = new StringBuilder();
        String currentFieldName = null;
        Class<?> currentFieldType = null;
        Map<String, Object> result = new HashMap<>();
        int currLevel = level;


        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            int charsRead = i - start;

            if (state == ParserState.START) {
                if (c == '{') {
                    state = ParserState.BEFORE_FIELD_NAME;
                    currLevel++;
                } else {
                    throw new IllegalArgumentException(String.format("Json object must start with a '{' but got %s", c));
                }
                continue;
            }

            if (state == ParserState.BEFORE_FIELD_NAME) {
                if (c == '"') {
                    state = ParserState.FIELD_NAME;
                } else if (c == '}') {
                    if (--currLevel == level) return new FieldDeserialization(charsRead, result);
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
                        // finish reading value if current character is unescaped quote
                        result.put(currentFieldName, sb.toString());
                        sb.setLength(0);
                        state = ParserState.BEFORE_FIELD_NAME;
                    } else {
                        // build string value
                        sb.append(c);
                    }
                } else if (currentFieldType.isRecord()) {
                    // if field is a record deserialize it recursively - passing i-1 to begin from '{')
                    var deserResult = deserialize(json, currentFieldType, i-1,level+1);
                    i += deserResult.charsRead() - 1;
                    result.put(currentFieldName, deserResult.obj);
                    sb.setLength(0);
                    state = ParserState.BEFORE_FIELD_NAME;
                } else {
                    // if field is not a string or a record it is considered finished when it's followed by comma, new line, or closing bracket
                    if (c == ',' || c == '\n' || c == '}') {
                        Object value = parseValue(sb.toString(), currentFieldType);
                        result.put(currentFieldName, value);
                        sb.setLength(0);
                        state = ParserState.BEFORE_FIELD_NAME;
                        if (c == '}' && --currLevel == level) return new FieldDeserialization(charsRead, result);
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        throw new IllegalStateException("Illegal deserialization state");
    }

    record FieldDeserialization(int charsRead, Map<String, Object> fields) {}
    record DeserResult<T>(int charsRead, T obj){}


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
