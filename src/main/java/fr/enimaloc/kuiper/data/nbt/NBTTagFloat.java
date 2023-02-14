/*
 * NBTTagFloat
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
public class NBTTagFloat extends NBTTag<Float> {
    public NBTTagFloat() {
    }

    public NBTTagFloat(String name, float value) {
        super(name, value);
    }

    @Override
    byte getID() {
        return 5;
    }

    @Override
    public void write0(BinaryWriter writer) {
        writer.writeFloat(getValue());
    }

    @Override
    public void read0(BinaryReader reader) {
        value = reader.readFloat();
    }
}
