/*
 * ClientboundPongResponsePacket
 *
 * 0.0.1
 *
 * 04/09/2022
 */
package fr.enimaloc.kuiper.network.packet.status;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;

/**
 *
 */
public class ClientboundPongResponsePacket extends SimpleClassDescriptor implements Packet.Clientbound {

    private long payload;

    @Override
    public int id() {
        return 0x01;
    }

    public ClientboundPongResponsePacket payload(long payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeLong(this.payload);
    }
}
