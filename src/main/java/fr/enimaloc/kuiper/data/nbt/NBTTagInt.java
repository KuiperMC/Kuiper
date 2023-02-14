/*
 * NBTTagInt
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.data.nbt;

import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;

/**
 *
 */
public class NBTTagInt extends NBTTag<Integer> {

    public NBTTagInt() {
    }

    public NBTTagInt(String name, int value) {
        super(name, value);
    }

    @Override
    public byte getID() {
        return 3;
    }

    @Override
    public void write0(BinaryWriter writer) {
        writer.writeInt(getValue());
    }

    @Override
    public void read0(BinaryReader reader) {
        value = reader.readInt();
    }
}
