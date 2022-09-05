/*
 * ClientboundLoginSuccess
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.login;

import fr.enimaloc.kuiper.entities.Player;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.network.data.Writeable;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class ClientboundLoginSuccess extends SimpleClassDescriptor implements Packet.Clientbound {

    private UUID           uuid;
    private String         username;
    private List<Property> properties;

    @Override
    public int id() {
        return 0x02;
    }

    public ClientboundLoginSuccess player(Player player) {
        this.uuid     = player.uuid();
        this.username = player.name();
        return this;
    }

    public ClientboundLoginSuccess uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ClientboundLoginSuccess username(String username) {
        this.username = username;
        return this;
    }

    public ClientboundLoginSuccess properties(List<Property> properties) {
        this.properties = properties;
        return this;
    }

    public ClientboundLoginSuccess property(Property property) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(property);
        return this;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeUUID(uuid);
        writer.writeString(username, SizedStrategy.VARINT);
        writer.writeList(properties, BinaryWriter::write, SizedStrategy.VARINT);
    }

    public record Property(String name, String value, String signature) implements Writeable {
        public Property(String name, String value) {
            this(name, value, null);
        }

        @Override
        public void write(BinaryWriter binaryWriter) {
            binaryWriter.writeString(name, SizedStrategy.VARINT);
            binaryWriter.writeString(value, SizedStrategy.VARINT);
            binaryWriter.writeBoolean(signature != null);
            if (signature != null) {
                binaryWriter.writeString(signature, SizedStrategy.VARINT);
            }
        }

    }
}
