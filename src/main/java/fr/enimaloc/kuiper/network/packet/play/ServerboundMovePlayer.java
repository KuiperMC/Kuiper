/*
 * ServerboundMovePlayer
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;

/**
 *
 */
public class ServerboundMovePlayer extends SimpleClassDescriptor implements Packet.Serverbound {

    public final double x;
    public final double y;
    public final double z;
    public final float yaw;
    public final float pitch;
    public final boolean onGround;

    public ServerboundMovePlayer(BinaryReader reader) {
        this.x = reader.readDouble();
        this.y = reader.readDouble();
        this.z = reader.readDouble();
        this.yaw = reader.readFloat();
        this.pitch = reader.readFloat();
        this.onGround = reader.readBoolean();
    }

    @Override
    public int id() {
        return 0x14;
    }

    @Override
    public void handle(Connection connection) {
        connection.player.x = x;
        connection.player.y = y;
        connection.player.z = z;
        connection.player.yaw = yaw;
        connection.player.pitch = pitch;
    }
}
