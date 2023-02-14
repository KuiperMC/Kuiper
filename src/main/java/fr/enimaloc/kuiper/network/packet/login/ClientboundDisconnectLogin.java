/*
 * ClientboundDisconnectLogin
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.network.packet.login;

import fr.enimaloc.kuiper.logger.PacketClassDescriptor;
import fr.enimaloc.kuiper.mojang.ChatObject;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;

import static fr.enimaloc.kuiper.constant.Objects.GSON;

/**
 *
 */
public class ClientboundDisconnectLogin extends PacketClassDescriptor implements Packet.Clientbound {

    private ChatObject reason;

    public ClientboundDisconnectLogin(ChatObject reason) {
        this.reason = reason;
    }

    public ClientboundDisconnectLogin(BinaryReader reader) {
        this.reason = GSON.fromJson(reader.readString(SizedStrategy.VARINT), ChatObject.class);
    }

    @Override
    public int getPacketId() {
        return 0x00;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeString(GSON.toJson(reason), SizedStrategy.VARINT);
    }
}
