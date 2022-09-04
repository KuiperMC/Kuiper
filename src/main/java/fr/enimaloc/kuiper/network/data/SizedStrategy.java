/*
 * SizedStrategy
 *
 * 0.0.1
 *
 * 03/09/2022
 */
package fr.enimaloc.kuiper.network.data;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 *
 */
public enum SizedStrategy {
    VARINT((i, writer) -> writer.writeVarInt(i.intValue()), BinaryReader::readVarInt),
    SHORT((i, writer) -> writer.writeShort(i.shortValue()), BinaryReader::readByte),
    INT((i, writer) -> writer.writeInt(i.intValue()), BinaryReader::readInt),
    NONE((i, writer) -> {}, null);

    public final BiConsumer<Number, BinaryWriter> writer;
    public final Function<BinaryReader, Number> reader;

    SizedStrategy(BiConsumer<Number, BinaryWriter> writer, Function<BinaryReader, Number> reader) {
        this.writer = writer;
        this.reader = reader;
    }
}
