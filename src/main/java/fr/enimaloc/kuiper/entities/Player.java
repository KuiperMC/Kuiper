/*
 * Player
 *
 * 0.0.1
 *
 * 02/09/2022
 */
package fr.enimaloc.kuiper.entities;

import fr.enimaloc.kuiper.chat.ChatMode;
import fr.enimaloc.kuiper.network.Connection;
import java.util.Locale;
import java.util.UUID;

/**
 *
 */
public class Player {

    public       Locale     locale;
    public       byte       viewDistance;
    public       ChatMode   chatMode;
    public       boolean    chatColors;
    public       byte       displayedSkinParts;
    public       boolean    rightMainHand;
    public       boolean    textFiltering;
    public       boolean    serverListing;
    public       String     brand;
    public       short      slot;
    private      String     name;
    private      UUID       uuid;
    public final Connection connection;

    public Player(Connection connection) {
        this.connection = connection;
    }

    public String name() {
        return name;
    }

    public Player name(String name) {
        this.name = name;
        return this;
    }

    public UUID uuid() {
        return uuid;
    }

    public Player uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public class Status {
        public static final byte HURT_ANIMATION             = 2;
        public static final byte DEATH_ANIMATION            = 3;
        public static final byte USE_FINISHED               = 9;
        public static final byte ENABLE_REDUCED_DEBUG_INFO  = 22;
        public static final byte DISABLE_REDUCED_DEBUG_INFO = 23;
        public static final byte OP_LEVEL_0                 = 24;
        public static final byte OP_LEVEL_1                 = 25;
        public static final byte OP_LEVEL_2                 = 26;
        public static final byte OP_LEVEL_3                 = 27;
        public static final byte OP_LEVEL_4                 = 28;
        public static final byte SHIELD_BLOCK               = 29;
        public static final byte SHIELD_BREAK               = 30;
        public static final byte THORNS_SOUND               = 33;
        public static final byte TOTEM_USE                  = 35;
        public static final byte DROWNING                   = 36;
        public static final byte BURNING                    = 37;
        public static final byte CLOUD_PARTICLE             = 43;
        public static final byte SWEET_BERRY_BUSH_HURT      = 44;
        public static final byte CHORUS_TELEPORT            = 46;
        public static final byte MAIN_HAND_BREAK            = 47;
        public static final byte OFF_HAND_BREAK             = 48;
        public static final byte HEAD_BREAK                 = 49;
        public static final byte CHEST_BREAK                = 50;
        public static final byte LEGS_BREAK                 = 51;
        public static final byte FEET_BREAK                 = 52;
        public static final byte HONEY_SLIDE                = 53;
        public static final byte HONEY_FALL                 = 54;
        public static final byte SWAP_HANDS                 = 55;
        public static final byte FREEZING                   = 57;
        public static final byte DEATH_PARTICLES            = 60;
    }
}
