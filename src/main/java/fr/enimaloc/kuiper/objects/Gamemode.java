/*
 * Gamemode
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.objects;

import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.Writeable;

/**
 *
 */
public enum Gamemode implements Writeable {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeByte((byte) ordinal());
    }

}
