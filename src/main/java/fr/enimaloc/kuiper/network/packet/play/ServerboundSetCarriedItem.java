/*
 * ServerboundSetCarriedItem
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
public class ServerboundSetCarriedItem extends SimpleClassDescriptor implements Packet.Serverbound {

    public final short slot;

    public ServerboundSetCarriedItem(BinaryReader reader) {
        this.slot = reader.readShort();
    }

    @Override
    public int id() {
        return 0x27;
    }

    @Override
    public void handle(Connection connection) {
        connection.player.slot = slot;
    }
}
