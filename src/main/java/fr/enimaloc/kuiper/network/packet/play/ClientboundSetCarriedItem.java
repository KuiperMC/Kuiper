/*
 * ClientboundSetCarriedItem
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
public class ClientboundSetCarriedItem extends SimpleClassDescriptor implements Packet.Clientbound {

    private byte slot;

    @Override
    public int id() {
        return 0x47;
    }

    public ClientboundSetCarriedItem slot(byte slot) {
        this.slot = slot;
        return this;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeByte(slot);
    }
}
