/*
 * NBTTagLong
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
public class NBTTagLong extends NBTTag<Long> {

    public NBTTagLong() {
    }

    public NBTTagLong(String name, long value) {
        super(name, value);
    }

    @Override
    public byte getID() {
        return 4;
    }

    @Override
    public void write0(BinaryWriter writer) {
        writer.writeLong(getValue());
    }

    @Override
    public void read0(BinaryReader reader) {
        value = reader.readLong();
    }
}
