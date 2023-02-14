/*
 * NBTTagByte
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
public class NBTTagByte extends NBTTag<Byte> {

    public NBTTagByte() {
    }

    public NBTTagByte(String name, byte value) {
        super(name, value);
    }

    @Override
    byte getID() {
        return 1;
    }

    @Override
    public void write0(BinaryWriter writer) {
        writer.writeByte(getValue());
    }

    @Override
    public void read0(BinaryReader reader) {
        value = reader.readByte();
    }
}
