/*
 * NBTTagShort
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
public class NBTTagShort extends NBTTag<Short> {

    public NBTTagShort() {
    }

    public NBTTagShort(String name, short value) {
        super(name, value);
    }

    @Override
    public byte getID() {
        return 2;
    }

    @Override
    public void write0(BinaryWriter writer) {
        writer.writeShort(getValue());
    }

    @Override
    public void read0(BinaryReader reader) {
        value = reader.readShort();
    }
}
