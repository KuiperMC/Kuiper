/*
 * Location
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.world;

import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.Writeable;
import fr.enimaloc.kuiper.objects.Identifier;

/**
 *
 */
public record Location(Identifier dimension, Position position) implements Writeable {
    public Location(Identifier dimension, long x, long y, long z) {
        this(dimension, new Position(x, y, z));
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.write(dimension);
        binaryWriter.write(position);
    }

    public static record Position(long x, long y, long z) implements Writeable {
        public Position(long encoded) {
            this(encoded >> 38, encoded << 52 >> 52, encoded << 26 >> 38);
        }

        public long encoded() {
            return ((x & 0x3FFFFFF) << 38) | ((z & 0x3FFFFFF) << 12) | (y & 0xFFF);
        }

        @Override
        public void write(BinaryWriter binaryWriter) {
              binaryWriter.writeLong(encoded());
        }

    }
}
