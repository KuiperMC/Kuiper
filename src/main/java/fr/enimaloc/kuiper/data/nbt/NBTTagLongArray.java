/*
 * NBTTagLongArray
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.data.nbt;

import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;

/**
 *
 */
public class NBTTagLongArray extends NBTTag<long[]> {
    public NBTTagLongArray() {
    }

    public NBTTagLongArray(String name, long[] value) {
        super(name, value);
    }

    @Override
    public void write0(BinaryWriter writer) {
        writer.writeLongArray(getValue(), SizedStrategy.INT);
    }

    @Override
    public void read0(BinaryReader reader) {
        value = reader.readLongArray(SizedStrategy.INT);
    }

    @Override
    byte getID() {
        return 12;
    }
}
