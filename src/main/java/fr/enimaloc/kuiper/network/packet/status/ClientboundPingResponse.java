/*
 * ClientboundPingResponse
 *
 * 0.0.1
 *
 * 09/02/2023
 */
package fr.enimaloc.kuiper.network.packet.status;

import fr.enimaloc.kuiper.logger.PacketClassDescriptor;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;

/**
 *
 */
public class ClientboundPingResponse extends PacketClassDescriptor implements Packet.Clientbound {

    private long payload;

    public ClientboundPingResponse() {}

    public ClientboundPingResponse(BinaryReader reader) {
        this.payload = reader.readLong();
    }

    public ClientboundPingResponse setPayload(long payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public int getPacketId() {
        return 0x01;
    }

    public long getPayload() {
        return payload;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeLong(payload);
    }
}
