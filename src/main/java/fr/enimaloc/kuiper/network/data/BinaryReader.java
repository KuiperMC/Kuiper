/*
 * BinaryReader
 *
 * 0.0.1
 *
 * 03/09/2022
 */
package fr.enimaloc.kuiper.network.data;

import fr.enimaloc.kuiper.utils.VarIntUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 *
 */
public class BinaryReader extends InputStream {
    private final ByteBuffer buffer;

    public BinaryReader(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public BinaryReader(byte[] bytes) {
        this(ByteBuffer.wrap(bytes));
    }

    public byte readByte() {
        return buffer.get();
    }

    public short readShort() {
        return buffer.getShort();
    }

    public int readInt() {
        return buffer.getInt();
    }

    public long readLong() {
        return buffer.getLong();
    }

    public float readFloat() {
        return buffer.getFloat();
    }

    public double readDouble() {
        return buffer.getDouble();
    }

    public boolean readBoolean() {
        return buffer.get() == 1;
    }

    public char readChar() {
        return buffer.getChar();
    }

    public int readUnsignedShort() {
        return buffer.getShort() & 0xFFFF;
    }

    public int readVarInt() {
        try {
            return Objects.requireNonNull(VarIntUtils.readVarInt(this)).value();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public long readVarLong() {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = buffer.get();
            long value = (read & 0b01111111);
            result |= (value << (7 * numRead));
            numRead++;
            if (numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while ((read & 0b10000000) != 0);
        return result;
    }

    public String readString(SizedStrategy strategy) {
        int length = strategy.reader.apply(this).intValue();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }

    public String[] readStringArray(SizedStrategy strategy, SizedStrategy stringStrategy) {
        int length = strategy.reader.apply(this).intValue();
        String[] strings = new String[length];
        for (int i = 0; i < length; i++) {
            strings[i] = readString(stringStrategy);
        }
        return strings;
    }

    public int[] readVarIntArray(SizedStrategy strategy) {
        int length = strategy.reader.apply(this).intValue();
        int[] ints = new int[length];
        for (int i = 0; i < length; i++) {
            ints[i] = readVarInt();
        }
        return ints;
    }

    public long[] readVarLongArray(SizedStrategy strategy) {
        int length = strategy.reader.apply(this).intValue();
        long[] longs = new long[length];
        for (int i = 0; i < length; i++) {
            longs[i] = readVarLong();
        }
        return longs;
    }

    public long[] readLongArray(SizedStrategy strategy) {
        int length = strategy.reader.apply(this).intValue();
        long[] longs = new long[length];
        for (int i = 0; i < length; i++) {
            longs[i] = readLong();
        }
        return longs;
    }

    public int[] readIntArray(SizedStrategy strategy) {
        int length = strategy.reader.apply(this).intValue();
        int[] ints = new int[length];
        for (int i = 0; i < length; i++) {
            ints[i] = readInt();
        }
        return ints;
    }

    public byte[] readByteArray(SizedStrategy strategy) {
        int length = strategy.reader.apply(this).intValue();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    public byte[] readRemaining() {
        return readBytes(buffer.remaining());
    }

    public UUID readUUID() {
        return new UUID(readLong(), readLong());
    }

    public <T extends Readable> T read(Supplier<T> supplier) {
        T t = supplier.get();
        t.read(this);
        return t;
    }

    public <T extends Readable> T[] readArray(SizedStrategy strategy, Supplier<T> supplier) {
        int                                length = strategy.reader.apply(this).intValue();
        @SuppressWarnings("unchecked") T[] array  = (T[]) new Readable[length];
        for (int i = 0; i < length; i++) {
            array[i] = read(supplier);
        }
        return array;
    }

    public <T> List<T> readList(SizedStrategy strategy, Supplier<T> supplier) {
        int length = strategy.reader.apply(this).intValue();
        List<T> list = new java.util.ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(supplier.get());
        }
        return list;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public int read() {
        return readByte() & 0xFF;
    }

    @Override
    public int available() {
        return buffer.remaining();
    }

    public byte[] extractBytes(Runnable extractor) {
        int startingPosition = buffer.position();
        extractor.run();
        int endingPosition = getBuffer().position();
        byte[] output = new byte[endingPosition - startingPosition];
        buffer.get(output, 0, output.length);
        return output;
    }
}
