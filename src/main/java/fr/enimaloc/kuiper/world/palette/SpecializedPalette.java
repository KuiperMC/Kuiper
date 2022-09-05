/*
 * FilledPalette
 *
 * 0.0.1
 *
 * 01/09/2022
 */
package fr.enimaloc.kuiper.world.palette;

import java.util.function.IntUnaryOperator;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public interface SpecializedPalette extends Palette {
    @Override
    default int bitsPerEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    default int maxBitsPerEntry() {
        throw new UnsupportedOperationException();
    }

    interface Immutable extends SpecializedPalette {
        @Override
        default void set(int x, int y, int z, int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        default void fill(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        default void setAll(@NotNull EntrySupplier supplier) {
            throw new UnsupportedOperationException();
        }

        @Override
        default void replace(int x, int y, int z, @NotNull IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        default void replaceAll(@NotNull EntryFunction function) {
            throw new UnsupportedOperationException();
        }
    }
}
