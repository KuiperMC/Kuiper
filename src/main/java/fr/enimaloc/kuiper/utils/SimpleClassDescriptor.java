/*
 * SimpleClassDescriptor
 *
 * 0.0.1
 *
 * 03/09/2022
 */
package fr.enimaloc.kuiper.utils;

import java.lang.reflect.Field;
import java.util.StringJoiner;

/**
 *
 */
public
class SimpleClassDescriptor {
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", getClass().getSimpleName() + "{", "}");
        for (Field field : getClass().getDeclaredFields()) {
            try {
                joiner.add(field.getName()
                           + (field.getDeclaringClass() == String.class ? "='" : "=")
                           + (field.trySetAccessible() ? field.get(this) : "non-accessible")
                           + (field.getDeclaringClass() == String.class ? "'" : ""));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return joiner.toString();
    }
}
