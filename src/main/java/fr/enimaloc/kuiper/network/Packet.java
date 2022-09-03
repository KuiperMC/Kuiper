/*
 * Packet
 *
 * 0.0.1
 *
 * 02/09/2022
 */
package fr.enimaloc.kuiper.network;

/**
 *
 */
public interface Packet {

    int id();

    public interface Clientbound extends Packet{
        byte[] toBytes();
    }

    public interface Serverbound extends Packet {
        void handle(Connection connection);
    }
}
