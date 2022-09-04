/*
 * PacketNotFoundException
 *
 * 0.0.1
 *
 * 04/09/2022
 */
package fr.enimaloc.kuiper.exception;

import fr.enimaloc.kuiper.GameState;
import fr.enimaloc.kuiper.network.Packet;

/**
 *
 */
public class PacketNotFoundException extends RuntimeException {

    public PacketNotFoundException(int id, Packet.Type type, GameState state) {
        super("0x%02x %s packet not found for %s state".formatted(id, type, state));
    }

    public PacketNotFoundException(String message) {
        super(message);
    }
}
