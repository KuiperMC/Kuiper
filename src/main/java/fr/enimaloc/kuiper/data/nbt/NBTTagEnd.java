/*
 * NBTTagEnd
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
public class NBTTagEnd extends NBTTag<Void> {
    public NBTTagEnd() {
        super(null, null);
    }

    @Override
    public byte getID() {
        return 0;
    }

    @Override
    protected void read0(BinaryReader reader) {}

    @Override
    protected void write0(BinaryWriter writer) {}
}
