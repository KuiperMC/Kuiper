/*
 * MojangAuth
 *
 * 0.0.1
 *
 * 04/09/2022
 */
package fr.enimaloc.kuiper.extra;

import fr.enimaloc.kuiper.extra.mojangAuth.MojangCrypt;
import java.security.KeyPair;
import org.jetbrains.annotations.Nullable;

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
