/*
 * Identifier
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.objects;

import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.network.data.Writeable;

/**
 *
 */
public record Identifier(String namespace, String value) implements Writeable {
    public Identifier {
        if (!namespace.matches("[a-z0-9.-_]*")) {
            throw new IllegalArgumentException("Namespace must match [a-z0-9.-_]*");
        }
        if (!value.matches("[a-z0-9.-_/]*")) {
            throw new IllegalArgumentException("Value must match [a-z0-9.-_/]*");
        }
    }

    public Identifier(String identifier) {
        this(identifier.split(":")[0], identifier.split(":")[1]);
    }

    public static Identifier minecraft(String value) {
        return new Identifier("minecraft", value);
    }

    public static Identifier kuiper(String value) {
        return new Identifier("kuiper", value);
    }

    @Override
    public String toString() {
        return namespace + ":" + value;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeString(toString(), SizedStrategy.VARINT);
    }

}
