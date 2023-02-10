/*
 * ServerboundStatusRequest
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
public class ServerboundStatusRequest extends PacketClassDescriptor implements Packet.Serverbound {

    public ServerboundStatusRequest() {}

    public ServerboundStatusRequest(BinaryReader binaryReader) {
    }

    @Override
    public int getPacketId() {
        return 0x00;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
    }

    @Override
    public void handle(Connection connection) {
        connection.sendPacket(new ClientboundStatusResponse()
                .setVersion("ALPHA", 759)
                .setPlayers(0,
                        "enimaloc",
                        "kuiper",
                        "minecraft",
                        "java",
                        "packet",
                        "network",
                        "fr",
                        "en"
        ));
    }
}
