/*
 * ClientboundWorldBorder
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
public class ClientboundInitializeBorder extends SimpleClassDescriptor implements Packet.Clientbound {

    private double x;
    private double z;
    private double oldDiameter;
    private double newDiameter;
    private int    speed;
    private int    portalTeleportBoundary = 29999984;
    private int    warningBlocks;
    private int    warningTime;

    @Override
    public int id() {
        return 0x1D;
    }

    public ClientboundInitializeBorder x(double x) {
        this.x = x;
        return this;
    }

    public ClientboundInitializeBorder z(double z) {
        this.z = z;
        return this;
    }

    public ClientboundInitializeBorder center(Location.Position position) {
        return center(position.x(), position.z());
    }

    public ClientboundInitializeBorder center(double x, double z) {
        this.x = x;
        this.z = z;
        return this;
    }

    public ClientboundInitializeBorder oldDiameter(double oldDiameter) {
        this.oldDiameter = oldDiameter;
        return this;
    }

    public ClientboundInitializeBorder newDiameter(double newDiameter) {
        this.newDiameter = newDiameter;
        return this;
    }

    public ClientboundInitializeBorder speed(int speed) {
        this.speed = speed;
        return this;
    }

    public ClientboundInitializeBorder portalTeleportBoundary(int portalTeleportBoundary) {
        this.portalTeleportBoundary = portalTeleportBoundary;
        return this;
    }

    public ClientboundInitializeBorder warningBlocks(int warningBlocks) {
        this.warningBlocks = warningBlocks;
        return this;
    }

    public ClientboundInitializeBorder warningTime(int warningTime) {
        this.warningTime = warningTime;
        return this;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeDouble(this.x);
        binaryWriter.writeDouble(this.z);
        binaryWriter.writeDouble(this.oldDiameter);
        binaryWriter.writeDouble(this.newDiameter);
        binaryWriter.writeVarInt(this.speed);
        binaryWriter.writeVarInt(this.portalTeleportBoundary);
        binaryWriter.writeVarInt(this.warningBlocks);
        binaryWriter.writeVarInt(this.warningTime);
    }
}
