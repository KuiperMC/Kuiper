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
        for (Field field : getClass().getFields()) {
            try {
                joiner.add(field.getName()
                           + (field.getDeclaringClass() == String.class ? "='" : "=")
                           + field.get(this)
                           + (field.getDeclaringClass() == String.class ? "'" : ""));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return joiner.toString();
    }
}
