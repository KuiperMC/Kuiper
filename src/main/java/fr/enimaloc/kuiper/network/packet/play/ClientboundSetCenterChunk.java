/*
 * ClientboundSetCenteChunk
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
public class ClientboundSetCenterChunk extends SimpleClassDescriptor implements Packet.Clientbound {

    private int x;
    private int z;

    @Override
    public int id() {
        return 0x48;
    }

    public ClientboundSetCenterChunk x(int x) {
        this.x = x;
        return this;
    }

    public ClientboundSetCenterChunk z(int z) {
        this.z = z;
        return this;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeVarInt(x);
        writer.writeVarInt(z);
    }
}
