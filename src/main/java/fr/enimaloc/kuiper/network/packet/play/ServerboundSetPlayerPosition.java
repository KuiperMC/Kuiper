/*
 * ServerboundSetPlayerPosition
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
public class ServerboundSetPlayerPosition extends SimpleClassDescriptor implements Packet.Serverbound {

    public final double  x;
    public final double  y;
    public final double  z;
    public final boolean onGround;

    public ServerboundSetPlayerPosition(BinaryReader reader) {
        this.x        = reader.readDouble();
        this.y        = reader.readDouble();
        this.z        = reader.readDouble();
        this.onGround = reader.readBoolean();
    }

    @Override
    public int id() {
        return 0x13;
    }

    @Override
    public void handle(Connection connection) {
        connection.player.x = x;
        connection.player.y = y;
        connection.player.z = z;
    }
}
