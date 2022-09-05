/*
 * FilledPalette
 *
 * 0.0.1
 *
 * 01/09/2022
 */
package fr.enimaloc.kuiper.world.palette;

import fr.enimaloc.kuiper.network.data.BinaryWriter;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
record FilledPalette(byte dim, int value) implements SpecializedPalette.Immutable {
    @Override
    public int get(int x, int y, int z) {
        return value;
    }

    @Override
    public void getAll(@NotNull EntryConsumer consumer) {
        final byte dimension = this.dim;
        final int value = this.value;
        for (byte y = 0; y < dimension; y++)
            for (byte z = 0; z < dimension; z++)
                for (byte x = 0; x < dimension; x++)
                     consumer.accept(x, y, z, value);
    }

    @Override
    public void getAllPresent(@NotNull EntryConsumer consumer) {
        if (value != 0) getAll(consumer);
    }

    @Override
    public int count() {
        return value != 0 ? maxSize() : 0;
    }

    @Override
    public int dimension() {
        return dim;
    }

    @Override
    public @NotNull SpecializedPalette clone() {
        return this;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeByte((byte) 0);
        writer.writeVarInt(value);
        writer.writeVarInt(0);
    }
}