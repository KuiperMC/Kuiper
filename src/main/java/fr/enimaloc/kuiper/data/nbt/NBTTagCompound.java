/*
 * NBTTagCompound
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
public class NBTTagCompound extends NBTTag<List<NBTTag<?>>> {
    public NBTTagCompound() {
        this(null, new ArrayList<>());
    }

    public NBTTagCompound(String name) {
        this(name, new ArrayList<>());
    }

    public NBTTagCompound(String name, List<NBTTag<?>> value) {
        super(name, value);
    }

    public void add(NBTTag<?> tag) {
        this.getValue().add(tag);
    }

    public void remove(NBTTag<?> tag) {
        this.getValue().remove(tag);
    }

    public void remove(String name) {
        this.getValue().removeIf(tag -> tag.getName().equals(name));
    }

    public void remove(int index) {
        this.getValue().remove(index);
    }

    public NBTTag<?> get(String name) {
        for (NBTTag<?> tag : this.getValue()) {
            if (tag.getName().equals(name)) {
                return tag;
            }
        }
        return null;
    }

    public NBTTag<?> get(int index) {
        return this.getValue().get(index);
    }

    @Override
    public void read0(BinaryReader reader) {
        while (true) {
            NBTTag<?> tag = createTag(reader.readByte());
            if (tag instanceof NBTTagEnd) {
                break;
            }
            tag.read(reader);
            this.getValue().add(tag);
        }
    }

    @Override
    public void write0(BinaryWriter writer) {
        for (NBTTag<?> tag : getValue()) {
            tag.write(writer);
        }
        new NBTTagEnd().write(writer);
    }

    @Override
    byte getID() {
        return 10;
    }
}
