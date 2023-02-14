/*
 * MojangAuth
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.mojang.auth;

import org.jetbrains.annotations.Nullable;

import java.security.KeyPair;

/**
 *
 */
public class MojangAuth {
    public static final     String  AUTH_URL = System.getProperty("KUIPER.AUTH_URL",
                    "https://sessionserver.mojang.com/session/minecraft/hasJoined")
            .concat("?username=%s&serverId=%s");
    private static volatile boolean enabled  = false;
    private static volatile KeyPair keyPair;

    /**
     * Enables mojang authentication on the server.
     * <p>
     * Be aware that enabling a proxy will make Mojang authentication ignored.
     */
    public static void init() {
//        Check.stateCondition(enabled, "Mojang auth is already enabled!");
        MojangAuth.enabled = true;
        // Generate necessary fields...
        MojangAuth.keyPair = MojangCrypt.generateKeyPair();
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static @Nullable KeyPair getKeyPair() {
        return keyPair;
    }
}
