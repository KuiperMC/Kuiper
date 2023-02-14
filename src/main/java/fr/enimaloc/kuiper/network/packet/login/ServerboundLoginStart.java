/*
 * ServerboundLoginStart
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.network.packet.login;

import fr.enimaloc.kuiper.logger.PacketClassDescriptor;
import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 */
public class ServerboundLoginStart extends PacketClassDescriptor implements Packet.Serverbound {

    private           String name;
    private @Nullable UUID   uuid;

    public ServerboundLoginStart(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public ServerboundLoginStart(BinaryReader reader) {
        this.name = reader.readString(SizedStrategy.VARINT);
        this.uuid = reader.readBoolean() ? reader.readUUID() : null;
    }

    @Override
    public int getPacketId() {
        return 0x00;
    }

    @Override
    public void handle(Connection connection) {
        connection.player = connection.server.getEntitiesManager().createPlayer(connection);
        if (connection.server.getSettings().onlineMode) {
            byte[] nonce = new byte[4];
            ThreadLocalRandom.current().nextBytes(nonce);
            connection.data.put("nonce", nonce);
            connection.sendPacket(new ClientboundEncryptionRequest(nonce));
        } else {
            connection.sendPacket(new ClientboundLoginSuccess(UUID.randomUUID(), name));
        }
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeString(name, SizedStrategy.VARINT);
        binaryWriter.writeBoolean(uuid != null);
        if (uuid != null) {
            binaryWriter.writeUUID(uuid);
        }
    }
}
