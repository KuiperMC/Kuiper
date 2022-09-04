/*
 * ServerboundLoginStart
 *
 * 0.0.1
 *
 * 04/09/2022
 */
package fr.enimaloc.kuiper.network.packet.login;

import fr.enimaloc.kuiper.constant.Providers;
import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import java.util.UUID;

/**
 *
 */
public class ServerboundLoginStart extends SimpleClassDescriptor implements Packet.Serverbound {

    public final String  username;
    public final SigData sigData;
    public final UUID    uuid;

    public ServerboundLoginStart(BinaryReader reader) {
        this.username = reader.readString(SizedStrategy.VARINT);
        this.sigData  = reader.hasNext() && reader.readBoolean() ? new SigData(reader) : null;
        this.uuid     = reader.hasNext() && reader.readBoolean() ? reader.readUUID() : Providers.uuidProvider.get(this.username);
    }

    @Override
    public int id() {
        return 0x00;
    }

    @Override
    public void handle(Connection connection) {

    }

    public record SigData(long timestamp, byte[] publicKey, byte[] signature) {
        public SigData(BinaryReader reader) {
            this(reader.readLong(),
                 reader.readByteArray(SizedStrategy.VARINT),
                 reader.readByteArray(SizedStrategy.VARINT));
        }
    }
}
