/*
 * Slot
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.objects;

import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.Writeable;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import net.querz.nbt.tag.CompoundTag;

/**
 *
 */
public class Slot extends SimpleClassDescriptor implements Writeable {

    private int         id;
    private byte        count;
    private CompoundTag nbt;

    public Slot id(int id) {
        this.id = id;
        return this;
    }

    public Slot count(byte count) {
        this.count = count;
        return this;
    }

    public Slot nbt(CompoundTag nbt) {
        this.nbt = nbt;
        return this;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeBoolean(id >= 0);
        if (id >= 0) {
            writer.writeVarInt(id);
            writer.writeByte(count);
            if (nbt == null) {
                nbt = new CompoundTag();
            }
            writer.writeNBT(nbt);
        }
    }
}
