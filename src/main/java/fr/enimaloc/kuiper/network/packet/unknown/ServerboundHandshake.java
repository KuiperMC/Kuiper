/*
 * ServerboundHandshake
 *
 * 0.0.1
 *
 * 09/02/2023
 */
package fr.enimaloc.kuiper.network.packet.unknown;

import fr.enimaloc.kuiper.GameState;
import fr.enimaloc.kuiper.logger.PacketClassDescriptor;
import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;

/**
 *
 */
public class ServerboundHandshake extends PacketClassDescriptor implements Packet.Serverbound {

    private int       protocolVersion;
    private String    serverAddress;
    private int       serverPort;
    private GameState state;

    public ServerboundHandshake() {}

    public ServerboundHandshake(BinaryReader reader) {
        this.protocolVersion = reader.readVarInt();
        this.serverAddress   = reader.readString(SizedStrategy.VARINT);
        this.serverPort      = reader.readUnsignedShort();
        this.state           = GameState.values()[reader.readVarInt()];
    }

    public ServerboundHandshake setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    public ServerboundHandshake setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        return this;
    }

    public ServerboundHandshake setServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public ServerboundHandshake setState(GameState state) {
        this.state = state;
        return this;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeVarInt(protocolVersion);
        binaryWriter.writeString(serverAddress, SizedStrategy.VARINT);
        binaryWriter.writeChar((char) serverPort);
        binaryWriter.writeVarInt(state.ordinal());
    }

    @Override
    public int getPacketId() {
        return 0x00;
    }

    @Override
    public void handle(Connection connection) {
        connection.gameState = state;
    }
}
