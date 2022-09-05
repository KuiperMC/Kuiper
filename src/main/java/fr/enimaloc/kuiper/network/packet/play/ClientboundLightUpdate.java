/*
 * ClientboundLightUpdate
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
public class ClientboundLightUpdate extends SimpleClassDescriptor implements Packet.Clientbound {

    private int             x;
    private int             z;
    private Chunk.LightData lightData;

    @Override
    public int id() {
        return 0x22;
    }

    public ClientboundLightUpdate chunk(Chunk chunk) {
        this.x         = chunk.x();
        this.z         = chunk.z();
        this.lightData = chunk.lightData();
        return this;
    }

    public ClientboundLightUpdate x(int x) {
        this.x = x;
        return this;
    }

    public ClientboundLightUpdate z(int z) {
        this.z = z;
        return this;
    }

    public ClientboundLightUpdate lightData(Chunk.LightData lightData) {
        this.lightData = lightData;
        return this;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeVarInt(x);
        binaryWriter.writeVarInt(z);
        binaryWriter.write(lightData);
    }
}
