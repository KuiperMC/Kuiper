/*
 * NBTTagDouble
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
public class NBTTagDouble extends NBTTag<Double> {
    public NBTTagDouble() {
    }

    public NBTTagDouble(String name, double value) {
        super(name, value);
    }

    @Override
    public byte getID() {
        return 6;
    }

    @Override
    public void write0(BinaryWriter writer) {
        writer.writeDouble(getValue());
    }

    @Override
    public void read0(BinaryReader reader) {
        value = reader.readDouble();
    }
}
