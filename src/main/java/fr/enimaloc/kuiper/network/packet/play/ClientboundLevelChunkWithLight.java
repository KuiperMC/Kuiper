/*
 * ClientboundLevelChunkWithLight
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import fr.enimaloc.kuiper.world.Chunk;

/**
 *
 */
public class ClientboundLevelChunkWithLight extends SimpleClassDescriptor implements Packet.Clientbound {

    private Chunk chunk;

    @Override
    public int id() {
        return 0x1F;
    }
    public ClientboundLevelChunkWithLight chunk(Chunk chunk) {
        this.chunk = chunk;
        return this;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.write(chunk);
    }
}
