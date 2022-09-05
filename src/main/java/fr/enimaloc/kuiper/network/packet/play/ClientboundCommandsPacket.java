/*
 * ClientboundCommandsPacket
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;

/**
 *
 */
public class ClientboundCommandsPacket extends SimpleClassDescriptor implements Packet.Clientbound {
    @Override
    public int id() {
        return 0x0F;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeVarInt(0);
        writer.writeVarInt(0);
    }
}
