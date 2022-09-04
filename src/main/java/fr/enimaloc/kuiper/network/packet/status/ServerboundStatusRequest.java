/*
 * ServerboundStatusRequest
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
import java.awt.*;

/**
 *
 */
public class ServerboundStatusRequest extends SimpleClassDescriptor implements Packet.Serverbound {
    public ServerboundStatusRequest(BinaryReader reader) {
        // Nothing to read
    }

    @Override
    public int id() {
        return 0x00;
    }

    @Override
    public void handle(Connection connection) {
        connection.sendPacket(
                new ClientboundStatusResponsePacket().favicon(
                        ClientboundStatusResponsePacket.Favicon.Creator.random()
                                                                       .fill(64 / 4, 64 / 4, 32, 32, Color.BLUE)
                                                                       .create()));
    }
}
