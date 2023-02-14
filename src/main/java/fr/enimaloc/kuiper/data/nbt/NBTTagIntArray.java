/*
 * NBTTagIntArray
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
public class NBTTagIntArray extends NBTTag<int[]> {
    public NBTTagIntArray() {
    }

    public NBTTagIntArray(String name, int[] value) {
        super(name, value);
    }

    @Override
    public void write0(BinaryWriter writer) {
        writer.writeInt(getValue().length);
        for (int i : getValue()) {
            writer.writeInt(i);
        }
    }

    @Override
    protected void read0(BinaryReader reader) {
        int[] array = new int[reader.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = reader.readInt();
        }
        value = array;
    }

    @Override
    byte getID() {
        return 11;
    }
}
