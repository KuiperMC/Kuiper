/*
 * BinaryWriter
 *
 * 0.0.1
 *
 * 03/09/2022
 */
package fr.enimaloc.kuiper.network.data;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 */
public class BinaryWriter extends OutputStream {

    private final boolean    resizable;
    private       ByteBuffer buffer;

    public BinaryWriter(ByteBuffer buffer, boolean resizable) {
        this.resizable = resizable;
        this.buffer    = buffer;
    }

    public BinaryWriter(ByteBuffer buffer) {
        this(buffer, true);
    }

    public BinaryWriter(int size) {
        this(ByteBuffer.allocate(size));
    }

    public BinaryWriter() {
        this(255);
    }

    protected void ensureSize(int length) {
        if (!resizable) {
            return;
        }
        final int position = buffer.position();
        if (position + length >= buffer.limit()) {
            final int newLength = (position + length) * 4;
            var copy = buffer.isDirect() ?
                    ByteBuffer.allocateDirect(newLength) : ByteBuffer.allocate(newLength);
            copy.put(buffer.flip());
            this.buffer = copy;
        }
    }

    public BinaryWriter writeByte(byte b) {
        ensureSize(Byte.BYTES);
        buffer.put(b);
        return this;
    }

    public BinaryWriter writeBoolean(boolean b) {
        return writeByte((byte) (b ? 1 : 0));
    }

    public BinaryWriter writeChar(char c) {
        ensureSize(Character.BYTES);
        buffer.putChar(c);
        return this;
    }

    public BinaryWriter writeShort(short s) {
        ensureSize(Short.BYTES);
        buffer.putShort(s);
        return this;
    }

    public BinaryWriter writeInt(int i) {
        ensureSize(Integer.BYTES);
        buffer.putInt(i);
        return this;
    }

    public BinaryWriter writeLong(long l) {
        ensureSize(Long.BYTES);
        buffer.putLong(l);
        return this;
    }

    public BinaryWriter writeFloat(float f) {
        ensureSize(Float.BYTES);
        buffer.putFloat(f);
        return this;
    }

    public BinaryWriter writeDouble(double d) {
        ensureSize(Double.BYTES);
        buffer.putDouble(d);
        return this;
    }

    public BinaryWriter writeVarInt(int i) {
        ensureSize(5);
        while ((i & 0xFFFFFF80) != 0L) {
            buffer.put((byte) (i & 0x7F | 0x80));
            i >>>= 7;
        }
        buffer.put((byte) i);
        return this;
    }

    public BinaryWriter writeVarLong(long l) {
        ensureSize(10);
        while ((l & 0xFFFFFFFFFFFFFF80L) != 0L) {
            buffer.put((byte) (l & 0x7F | 0x80));
            l >>>= 7;
        }
        buffer.put((byte) l);
        return this;
    }

    public BinaryWriter writeString(String s) {
        return writeString(s, SizedStrategy.NONE);
    }

    public BinaryWriter writeNullTerminatedString(String s) {
        return writeString(s + '\0');
    }

    public BinaryWriter writeString(String s, SizedStrategy strategy) {
        final byte[] bytes = s.getBytes();
        strategy.writer.accept(bytes.length, this);
        writeBytes(bytes);
        return this;
    }

    public BinaryWriter writeVarIntArray(int[] array, SizedStrategy strategy) {
        if (array == null) {
            strategy.writer.accept(0, this);
            return this;
        }
        strategy.writer.accept(array.length, this);
        for (int i : array) {
            writeVarInt(i);
        }
        return this;
    }

    public BinaryWriter writeVarLongArray(long[] array, SizedStrategy strategy) {
        if (array == null) {
            strategy.writer.accept(0, this);
            return this;
        }
        strategy.writer.accept(array.length, this);
        for (long l : array) {
            writeVarLong(l);
        }
        return this;
    }

    public BinaryWriter writeByteArray(byte[] array, SizedStrategy strategy) {
        if (array == null) {
            strategy.writer.accept(0, this);
            return this;
        }
        strategy.writer.accept(array.length, this);
        ensureSize(array.length);
        buffer.put(array);
        return this;
    }

    public BinaryWriter writeBytes(byte[] array) {
        return writeByteArray(array, SizedStrategy.NONE);
    }

    public BinaryWriter writeStringArray(String[] array, SizedStrategy stringStrategy, SizedStrategy strategy) {
        if (array == null) {
            strategy.writer.accept(0, this);
            return this;
        }
        strategy.writer.accept(array.length, this);
        for (String s : array) {
            writeString(s, stringStrategy);
        }
        return this;
    }

    public BinaryWriter writeUUID(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public BinaryWriter write(Writeable writeable) {
        writeable.write(this);
        return this;
    }

    public BinaryWriter writeArray(Writeable[] array, SizedStrategy strategy) {
        if (array == null) {
            strategy.writer.accept(0, this);
            return this;
        }
        strategy.writer.accept(array.length, this);
        for (Writeable writeable : array) {
            write(writeable);
        }
        return this;
    }

    public BinaryWriter write(ByteBuffer buffer) {
        ensureSize(buffer.remaining());
        this.buffer.put(buffer);
        return this;
    }

    public BinaryWriter write(BinaryWriter writer) {
        return write(writer.buffer);
    }

    public <T> BinaryWriter writeList(Collection<T> list, BiConsumer<BinaryWriter, T> consumer, SizedStrategy strategy) {
        if (list == null) {
            strategy.writer.accept(0, this);
            return this;
        }
        strategy.writer.accept(list.size(), this);
        for (T t : list) {
            consumer.accept(this, t);
        }
        return this;
    }

    public byte[] toByteArray() {
        buffer.flip();
        final byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

    public BinaryWriter writeAtStart(BinaryWriter header) {
        return setBuffer(concat(header.getBuffer(), buffer));
    }

    public BinaryWriter writeAtEnd(BinaryWriter footer) {
        return setBuffer(concat(buffer, footer.getBuffer()));
    }

    public static ByteBuffer concat(final ByteBuffer... buffers) {
        final ByteBuffer combined = ByteBuffer.allocate(Arrays.stream(buffers).mapToInt(Buffer::remaining).sum());
        Arrays.stream(buffers).forEach(b -> combined.put(b.duplicate()));
        return combined;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public BinaryWriter setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
        return this;
    }

    @Override
    public void write(int b) throws IOException {
        writeByte((byte) b);
    }

    public static byte[] makeArray(Consumer<BinaryWriter> writing) {
        final BinaryWriter writer = new BinaryWriter();
        writing.accept(writer);
        return writer.toByteArray();
    }

    private BinaryWriter writeSizedStrategy(int length, SizedStrategy strategy) {
        switch (strategy) {
            case VARINT:
                writeVarInt(length);
                break;
            case SHORT:
                writeShort((short) length);
                break;
            case INT:
                writeInt(length);
                break;
            case NONE:
                break;
        }
        return this;
    }

}
