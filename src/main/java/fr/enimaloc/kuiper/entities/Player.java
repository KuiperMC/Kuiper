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

    public Locale locale;
    public byte viewDistance;
    public ChatMode chatMode;
    public boolean chatColors;
    public byte displayedSkinParts;
    public boolean rightMainHand;
    public boolean textFiltering;
    public boolean serverListing;
    private String name;
    private UUID   uuid;
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
}
