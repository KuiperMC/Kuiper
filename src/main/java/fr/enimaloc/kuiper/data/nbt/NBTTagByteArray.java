/*
 * NBTTagByteArray
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
public class NBTTagByteArray extends NBTTag<byte[]> {
    public NBTTagByteArray() {
    }

    public NBTTagByteArray(String name, byte[] value) {
        super(name, value);
    }

    @Override
    byte getID() {
        return 7;
    }

    @Override
    public void write0(BinaryWriter writer) {
        writer.writeByteArray(getValue(), SizedStrategy.INT);
    }

    @Override
    public void read0(BinaryReader reader) {
        value = reader.readByteArray(SizedStrategy.INT);
    }
}
