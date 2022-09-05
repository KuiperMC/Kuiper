/*
 * ClientboundChangeDifficulty
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import fr.enimaloc.kuiper.world.Difficulty;

/**
 *
 */
public class ClientboundChangeDifficulty extends SimpleClassDescriptor implements Packet.Clientbound {

    private byte    difficulty;
    private boolean locked;

    @Override
    public int id() {
        return 0x0B;
    }

    public ClientboundChangeDifficulty difficulty(Difficulty difficulty) {
        return difficulty((byte) difficulty.ordinal());
    }

    public ClientboundChangeDifficulty difficulty(byte difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public ClientboundChangeDifficulty locked(boolean locked) {
        this.locked = locked;
        return this;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeByte(difficulty);
        binaryWriter.writeBoolean(locked);
    }
}
