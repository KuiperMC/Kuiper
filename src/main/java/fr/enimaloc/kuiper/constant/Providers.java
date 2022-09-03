/*
 * Provider
 *
 * 0.0.1
 *
 * 03/09/2022
 */
package fr.enimaloc.kuiper.constant;

import java.util.UUID;

/**
 *
 */
public class Providers {

    public static Provider<String, UUID> uuidProvider = username -> UUID.nameUUIDFromBytes(username.getBytes());

    @FunctionalInterface
    public interface Provider<F, T> {
        T get(F f);
    }
}
