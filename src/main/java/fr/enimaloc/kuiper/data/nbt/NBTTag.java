/*
 * NBTTag
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.data.nbt;

import fr.enimaloc.kuiper.network.data.*;
import fr.enimaloc.kuiper.network.data.Readable;

/**
 *
 */
public abstract class NBTTag<T> implements Writeable, Readable {

    protected String name;
    protected T value;

    public NBTTag() {
    }

    public NBTTag(String name, T value) {
        this.name = name;
        this.value = value;
    }

    String getName() {
        return name;
    }

    T getValue() {
        return value;
    }

    abstract byte getID();

    @Override
    public final void read(BinaryReader reader) {
        if (getID() != 0) {
            name = reader.readString(SizedStrategy.SHORT);
            read0(reader);
        }
    }

    protected abstract void read0(BinaryReader reader);

    @Override
    public final void write(BinaryWriter writer) {
        writer.writeByte(getID());
        if (getID() != 0) {
            writer.writeString(getName(), SizedStrategy.SHORT);
            write0(writer);
        }
    }

    protected abstract void write0(BinaryWriter writer);

    @Override
    public String toString() {
        return "NBTTag{" + "name=" + name + ", value=" + value + '}';
    }

    public static NBTTag<?> createTag(byte type) {
        return switch (type) {
            case 0 -> new NBTTagEnd();
            case 1 -> new NBTTagByte();
            case 2 -> new NBTTagShort();
            case 3 -> new NBTTagInt();
            case 4 -> new NBTTagLong();
            case 5 -> new NBTTagFloat();
            case 6 -> new NBTTagDouble();
            case 7 -> new NBTTagByteArray();
            case 8 -> new NBTTagString();
            case 9 -> new NBTTagList();
            case 10 -> new NBTTagCompound();
            case 11 -> new NBTTagIntArray();
            case 12 -> new NBTTagLongArray();
            default -> throw new IllegalArgumentException("Unknown tag type: " + type);
        };
    }

    public static NBTTag<?> readTag(BinaryReader reader) {
        NBTTag<?> tag = createTag(reader.readByte());
        tag.read(reader);
        return tag;
    }
}
