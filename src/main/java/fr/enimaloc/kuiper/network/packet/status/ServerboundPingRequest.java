/*
 * ServerboundPingRequest
 *
 * 0.0.1
 *
 * 04/09/2022
 */
package fr.enimaloc.kuiper.network.packet.status;

import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;

/**
 *
 */
public class ServerboundPingRequest extends SimpleClassDescriptor implements Packet.Serverbound {

    public final long payload;

    public ServerboundPingRequest(BinaryReader reader) {
        this.payload = reader.readLong();
    }

    @Override
    public int id() {
        return 0x01;
    }

    @Override
    public void handle(Connection connection) {
        connection.sendPacket(new ClientboundPongResponsePacket().payload(this.payload));
        connection.terminate();
    }
}
