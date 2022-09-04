/*
 * VarIntUtils
 *
 * 0.0.1
 *
 * 03/09/2022
 */
package fr.enimaloc.kuiper.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class VarIntUtils {

    public static final int SEGMENT_BITS = 0x7F;
    public static final int CONTINUE_BIT = 0x80;

    VarIntUtils() {
    }

    public static VarInt readVarInt(InputStream inputStream) throws IOException {
        int  value    = 0;
        int  position = 0;
        byte currentByte;

        int length = 0;

        while (true) {
            byte[] nBytes = inputStream.readNBytes(1);
            if (nBytes.length == 0) {
                return null;
            }
            currentByte = nBytes[0];
            value |= (currentByte & SEGMENT_BITS) << position;
            length++;

            if ((currentByte & CONTINUE_BIT) == 0) {
                break;
            }

            position += 7;

            if (position >= 32) {
                throw new RuntimeException("VarInt is too big");
            }
        }
        return new VarInt(value, length);
    }

    public static int varIntSize(int i) {
        int result = 0;
        do {
            result++;
            i >>>= 7;
        } while (i != 0);
        return result;
    }

    public static byte[] getVarInt(int value) {
        byte[] bytes = new byte[varIntSize(value)];
        int    position = 0;
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                bytes[position] = (byte) value;
                return bytes;
            }

            bytes[position] = (byte) ((value & SEGMENT_BITS) | CONTINUE_BIT);
            position++;

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }
    }

    public record VarInt(int value, int length) {}
}
