/*
 * ServerboundPingRequest
 *
 * 0.0.1
 *
 * 09/02/2023
 */
package fr.enimaloc.kuiper.network.packet.status;

import fr.enimaloc.kuiper.logger.PacketClassDescriptor;
import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;

/**
 *
 */
public class ServerboundPingRequest extends PacketClassDescriptor implements Packet.Serverbound {
    private final long payload;

    public ServerboundPingRequest(BinaryReader reader) {
        this.payload = reader.readLong();
    }

    public ServerboundPingRequest(long payload) {
        this.payload = payload;
    }

    public ServerboundPingRequest() {
        this(System.currentTimeMillis());
    }

    @Override
    public int getPacketId() {
        return 0x01;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeLong(payload);
    }

    @Override
    public void handle(Connection connection) {
        connection.sendPacket(new ClientboundPingResponse().setPayload(payload));
    }
}
