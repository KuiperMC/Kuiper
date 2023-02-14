/*
 * Constant
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.constant;

/**
 *
 */
public class Constant {

    private Constant() {
    }

    public static final String NAME    = "Kuiper";
    public static final String VERSION = Version.getFormatted();

    public static final int    PROTOCOL_VERSION       = 761;
    public static final String MINECRAFT_VERSION_NAME = "1.19.3";

    public static class Version {
        private Version() {
        }

        public static final int MAJOR = 0;
        public static final int MINOR = 1;
        public static final int PATCH = 0;

        public static String getFormatted() {
            return String.format("%d.%d.%d", MAJOR, MINOR, PATCH);
        }
    }

}
