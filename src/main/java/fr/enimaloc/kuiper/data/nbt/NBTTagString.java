/*
 * NBTTagString
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.data.nbt;

import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;

import java.io.*;

/**
 *
 */
public class NBTTagString extends NBTTag<String> {
    public NBTTagString() {
    }

    public NBTTagString(String name, String value) {
        super(name, value);
    }

    @Override
    public byte getID() {
        return 8;
    }

    @Override
    public void write0(BinaryWriter writer) {
        try (DataOutputStream dos = new DataOutputStream(new OutputStream() {
            @Override
            public void write(int b) {
                writer.writeByte((byte) b);
            }
        })) {
            dos.writeUTF(getValue());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void read0(BinaryReader reader) {
        try (DataInputStream dis = new DataInputStream(new InputStream() {
            @Override
            public int read() {
                return reader.read();
            }
        })) {
            value = dis.readUTF();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
