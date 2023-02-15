/*
 * NBTTagList
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.data.nbt;

import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NBTTagList<T> extends NBTTag<List<NBTTag<T>>> {

    public NBTTagList() {
        this(null, new ArrayList<>());
    }

    public NBTTagList(String name, List<NBTTag<T>> value) {
        super(name, value);
    }

    @Override
    byte getID() {
        return 9;
    }

    public void add(NBTTag<T> tag) {
        this.getValue().add(tag);
    }

    public void remove(NBTTag<T> tag) {
        this.getValue().remove(tag);
    }

    public void remove(int index) {
        this.getValue().remove(index);
    }

    public NBTTag<?> get(int index) {
        return this.getValue().get(index);
    }

    @Override
    public void read0(BinaryReader reader) {
        byte type = reader.readByte();
        int size = reader.readInt();
        for (int i = 0; i < size; i++) {
            NBTTag<?> tag = createTag(type);
            tag.read0(reader);
            this.getValue().add((NBTTag<T>) tag);
        }
    }

    @Override
    public void write0(BinaryWriter writer) {
        writer.writeByte(getValue().isEmpty() ? 0 : get(0).getID());
        writer.writeInt(getValue().size());
        for (NBTTag<?> tag : getValue()) {
            tag.write0(writer);
        }
    }
}
