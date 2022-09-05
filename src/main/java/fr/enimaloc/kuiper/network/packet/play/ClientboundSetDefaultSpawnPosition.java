/*
 * ClientboundSetDefaultSpawnPosition
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import fr.enimaloc.kuiper.world.Location;

/**
 *
 */
public class ClientboundSetDefaultSpawnPosition extends SimpleClassDescriptor implements Packet.Clientbound {

    private Location.Position position;
    private float angle;

    @Override
    public int id() {
        return 0x4A;
    }

    public ClientboundSetDefaultSpawnPosition position(long x, long y, long z) {
        return position(new Location.Position(x, y, z));
    }

    public ClientboundSetDefaultSpawnPosition position(Location.Position position) {
        this.position = position;
        return this;
    }

    public ClientboundSetDefaultSpawnPosition angle(float angle) {
        this.angle = angle;
        return this;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.write(position);
        writer.writeFloat(angle);
    }
}
