/*
 * Manager
 *
 * 0.0.1
 *
 * 02/09/2022
 */
package fr.enimaloc.kuiper.collections;

import java.util.ArrayList;
import java.util.function.Function;

/**
 *
 */
public class Manager<E> extends ArrayList<E> {

    private final transient Function<Object[], E> provider;

    public Manager(Function<Object[], E> provider) {
        this.provider = provider;
    }

    public E create(Object... args) {
        E e = provider.apply(args);
        super.add(e);
        return e;
    }
}
