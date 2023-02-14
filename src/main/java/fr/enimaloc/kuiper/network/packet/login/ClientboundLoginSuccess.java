/*
 * ClientboundLoginSucess
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.network.packet.login;

import fr.enimaloc.kuiper.logger.PacketClassDescriptor;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;

import java.util.UUID;

/**
 *
 */
public class ClientboundLoginSuccess extends PacketClassDescriptor implements Packet.Clientbound {

    private UUID         uuid;
    private String       username;

    public ClientboundLoginSuccess(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public ClientboundLoginSuccess(BinaryReader reader) {
        this.uuid = reader.readUUID();
        this.username = reader.readString(SizedStrategy.VARINT);
    }

    @Override
    public int getPacketId() {
        return 0x02;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeUUID(uuid);
        binaryWriter.writeString(username, SizedStrategy.VARINT);
        binaryWriter.writeVarInt(0);
    }
}
