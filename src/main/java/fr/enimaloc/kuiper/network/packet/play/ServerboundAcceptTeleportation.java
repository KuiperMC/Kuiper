/*
 * ServerboundAcceptTeleportation
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
public class ServerboundAcceptTeleportation extends SimpleClassDescriptor implements Packet.Serverbound {

    public final int teleportId;

    public ServerboundAcceptTeleportation(BinaryReader reader) {
        this.teleportId = reader.readVarInt();
    }

    @Override
    public int id() {
        return 0x00;
    }

    @Override
    public void handle(Connection connection) {
    }
}
