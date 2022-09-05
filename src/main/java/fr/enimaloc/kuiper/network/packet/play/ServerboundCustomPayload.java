/*
 * ServerboundCustomPayload
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.objects.Identifier;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;

/**
 *
 */
public class ServerboundCustomPayload extends SimpleClassDescriptor implements Packet.Serverbound {

    public final Identifier channel;
    public final byte[]     data;

    public ServerboundCustomPayload(BinaryReader reader) {
        this.channel = new Identifier(reader.readString(SizedStrategy.VARINT));
        this.data    = reader.readByteArray(SizedStrategy.VARINT);
    }

    public String asString() {
        return new String(data);
    }

    @Override
    public int id() {
        return 0x0C;
    }

    @Override
    public void handle(Connection connection) {
        if (channel.equals(Identifier.minecraft("brand"))) {
            connection.player.brand = asString();
        }
    }
}
