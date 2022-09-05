/*
 * ClienboundSynchronizePosition
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
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class ClientboundSynchronizePosition extends SimpleClassDescriptor implements Packet.Clientbound {

    public static final AtomicInteger id = new AtomicInteger(0);

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private byte flags;
    private int teleportId = id.getAndIncrement();
    private boolean dismount;

    @Override
    public int id() {
        return 0x36;
    }

    public ClientboundSynchronizePosition x(double x) {
        this.x = x;
        return this;
    }

    public ClientboundSynchronizePosition y(double y) {
        this.y = y;
        return this;
    }

    public ClientboundSynchronizePosition z(double z) {
        this.z = z;
        return this;
    }

    public ClientboundSynchronizePosition position(Location.Position position) {
        this.x = position.x();
        this.y = position.y();
        this.z = position.z();
        return this;
    }

    public ClientboundSynchronizePosition yaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public ClientboundSynchronizePosition pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public ClientboundSynchronizePosition absolute(boolean x, boolean y, boolean z, boolean yaw, boolean pitch) {
        this.flags = 0x00;
        if (x) {
            this.flags |= 0x01;
        }
        if (y) {
            this.flags |= 0x02;
        }
        if (z) {
            this.flags |= 0x04;
        }
        if (yaw) {
            this.flags |= 0x08;
        }
        if (pitch) {
            this.flags |= 0x10;
        }
        return this;
    }

    public ClientboundSynchronizePosition teleportId(int teleportId) {
        this.teleportId = teleportId;
        return this;
    }

    public ClientboundSynchronizePosition dismount(boolean dismount) {
        this.dismount = dismount;
        return this;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeDouble(x);
        writer.writeDouble(y);
        writer.writeDouble(z);
        writer.writeFloat(yaw);
        writer.writeFloat(pitch);
        writer.writeByte(flags);
        writer.writeVarInt(teleportId);
        writer.writeBoolean(dismount);
    }
}
