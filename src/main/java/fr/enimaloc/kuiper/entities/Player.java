/*
 * Player
 *
 * 0.0.1
 *
 * 02/09/2022
 */
package fr.enimaloc.kuiper.entities;

import fr.enimaloc.kuiper.network.Connection;
import java.util.UUID;

/**
 *
 */
public class Player {

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
