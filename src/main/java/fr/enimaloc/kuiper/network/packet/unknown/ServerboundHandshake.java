/*
 * ServerboundHandshake
 *
 * 0.0.1
 *
 * 03/09/2022
 */
package fr.enimaloc.kuiper.network.packet.unknown;

import fr.enimaloc.kuiper.GameState;
import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;

/**
 *
 */
public class ServerboundHandshake extends SimpleClassDescriptor implements Packet.Serverbound {

    public final int       protocolVersion;
    public final String    serverAddress;
    public final int       serverPort;
    public final GameState state;

    public ServerboundHandshake(BinaryReader reader) {
        this.protocolVersion = reader.readVarInt();
        this.serverAddress   = reader.readString(SizedStrategy.VARINT);
        this.serverPort      = reader.readUnsignedShort();
        this.state           = GameState.values()[reader.readVarInt()];
    }

    @Override
    public int id() {
        return 0x00;
    }

    @Override
    public void handle(Connection connection) {
        connection.gameState = this.state;
    }
}
