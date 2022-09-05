/*
 * ClientboundRecipes
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
public class ClientboundRecipes extends SimpleClassDescriptor implements Packet.Clientbound {
    @Override
    public int id() {
        return 0x37;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeVarInt(0)
              .writeBoolean(false)
              .writeBoolean(false)
              .writeBoolean(false)
              .writeBoolean(false)
              .writeBoolean(false)
              .writeBoolean(false)
              .writeBoolean(false)
              .writeBoolean(false)
              .writeVarInt(0)
              .writeVarInt(0);
    }
}
