/*
 * ClientboundEntityEvent
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
public class ClientboundEntityEvent extends SimpleClassDescriptor implements Packet.Clientbound {

    private int entityId;
    private byte status;

    @Override
    public int id() {
        return 0x18;
    }

    public ClientboundEntityEvent entityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public ClientboundEntityEvent status(byte status) {
        this.status = status;
        return this;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeInt(entityId);
        binaryWriter.writeByte(status);
    }
}
