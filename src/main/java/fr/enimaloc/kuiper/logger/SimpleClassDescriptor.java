/*
 * SimpleClassDescriptor
 *
 * 0.0.1
 *
 * 03/09/2022
 */
package fr.enimaloc.kuiper.logger;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.StringJoiner;

/**
 *
 */
public
class SimpleClassDescriptor {
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", getClass().getSimpleName() + "{", "}");
        firstAdditionalFields().forEach((key, value) -> joiner.add(key + "=" + value));
        for (Field field : Arrays.stream(getClass().getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .toList()) {
            try {
                joiner.add(field.getName()
                        + (field.getDeclaringClass() == String.class ? "='" : "=")
                        + (field.trySetAccessible() ? getAsString(field) : "non-accessible")
                        + (field.getDeclaringClass() == String.class ? "'" : ""));
            } catch (IllegalAccessException e) {
                joiner.add(field.getName() + "=" + e);
            }
        }
        lastAdditionalFields().forEach((key, value) -> joiner.add(key + "=" + value));
        return joiner.toString();
    }

    protected Map<String, String> firstAdditionalFields() {
        return Collections.emptyMap();
    }

    protected Map<String, String> lastAdditionalFields() {
        return Collections.emptyMap();
    }

    private Object getAsString(Field field) throws IllegalAccessException {
        Object value = field.get(this);
        if (value == null) {
            value = "null";
        }
        if (value.getClass().isArray()) {
            Object[] array;
            if (value.getClass().getComponentType().isPrimitive()) {
                array = new Object[Array.getLength(value)];
                for (int i = 0; i < array.length; i++) {
                    array[i] = Array.get(value, i);
                }
            } else {
                array = (Object[]) value;
            }
            value = Arrays.toString(array);
        }
        return value;
    }
}
