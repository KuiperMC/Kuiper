/*
 * ClientboundPlayerInfo
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.network.data.Writeable;
import fr.enimaloc.kuiper.objects.Gamemode;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class ClientboundPlayerInfo extends SimpleClassDescriptor implements Packet.Clientbound {
    private List<Action> actions;

    @Override
    public int id() {
        return 0x34;
    }

    public ClientboundPlayerInfo actions(List<Action> actions) {
        this.actions = actions;
        return this;
    }

    public ClientboundPlayerInfo actions(Action... actions) {
        return actions(List.of(actions));
    }

    public ClientboundPlayerInfo addAction(Action action) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        if (!actions.isEmpty() && actions.get(0).id != action.id) {
            throw new IllegalArgumentException("All actions must have the same id");
        }
        actions.add(action);
        return this;
    }

    public ClientboundPlayerInfo add(UUID uuid, String name, Gamemode gamemode, int ping, String displayName) {
        return addAction(new AddPlayer().uuid(uuid)
                                        .name(name)
                                        .gamemode(gamemode)
                                        .ping(ping)
                                        .displayName(displayName));
    }

    public ClientboundPlayerInfo updateGamemode(UUID uuid, Gamemode gamemode) {
        return addAction(new UpdateGamemode().uuid(uuid)
                                             .gamemode(gamemode));
    }

    public ClientboundPlayerInfo updateLatency(UUID uuid, int ping) {
        return addAction(new UpdateLatency().uuid(uuid)
                                            .ping(ping));
    }

    public ClientboundPlayerInfo updateDisplayName(UUID uuid, String displayName) {
        return addAction(new UpdateDisplayName().uuid(uuid)
                                                .displayName(displayName));
    }

    public ClientboundPlayerInfo remove(UUID uuid) {
        return addAction(new RemovePlayer().uuid(uuid));
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeVarInt(actions.get(0).id);
        writer.writeList(actions, BinaryWriter::write, SizedStrategy.VARINT);
    }

    private abstract static class Action<T extends Action<?>> extends SimpleClassDescriptor implements Writeable {
        public final int id;
        public UUID uuid;

        private Action(int id) {
            this.id   = id;
        }

        public T uuid(UUID uuid) {
            this.uuid = uuid;
            return (T) this;
        }
    }

    public static class AddPlayer extends Action<AddPlayer> {
        public String name;
        public List<Property> properties;
        public int gamemode;
        public int ping;
        public String displayName;
        public SigData sigData;

        public AddPlayer() {
            super(0);
        }

        public AddPlayer name(String name) {
            this.name = name;
            return this;
        }

        public AddPlayer properties(List<Property> properties) {
            this.properties = properties;
            return this;
        }

        public AddPlayer properties(Property... properties) {
            this.properties = List.of(properties);
            return this;
        }

        public AddPlayer gamemode(int gamemode) {
            this.gamemode = gamemode;
            return this;
        }

        public AddPlayer gamemode(Gamemode gamemode) {
            return gamemode(gamemode.ordinal());
        }

        public AddPlayer ping(int ping) {
            this.ping = ping;
            return this;
        }

        public AddPlayer displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        @Override
        public void write(BinaryWriter writer) {
            writer.writeUUID(uuid);
            writer.writeString(name, SizedStrategy.VARINT);
            writer.writeList(properties, BinaryWriter::write, SizedStrategy.VARINT);
            writer.writeVarInt(gamemode);
            writer.writeVarInt(ping);
            writer.writeBoolean(displayName != null);
            if (displayName != null) {
                writer.writeString(displayName, SizedStrategy.VARINT);
            }
            writer.writeBoolean(sigData != null);
            if (sigData != null) {
                writer.write(sigData);
            }
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

        public record SigData(long timestamp, byte[] publicKey, byte[] signature) implements Writeable {
            @Override
            public void write(BinaryWriter binaryWriter) {
                binaryWriter.writeLong(timestamp);
                binaryWriter.writeByteArray(publicKey, SizedStrategy.VARINT);
                binaryWriter.writeByteArray(signature, SizedStrategy.VARINT);
            }
        }
    }

    public static class UpdateGamemode extends Action<UpdateGamemode> {
        public int gamemode;

        public UpdateGamemode() {
            super(1);
        }

        public UpdateGamemode gamemode(int gamemode) {
            this.gamemode = gamemode;
            return this;
        }

        public UpdateGamemode gamemode(Gamemode gamemode) {
            return gamemode(gamemode.ordinal());
        }

        @Override
        public void write(BinaryWriter writer) {
            writer.writeUUID(uuid);
            writer.writeVarInt(gamemode);
        }
    }

    public static class UpdateLatency extends Action<UpdateLatency> {
        public int ping;

        public UpdateLatency() {
            super(2);
        }

        public UpdateLatency ping(int ping) {
            this.ping = ping;
            return this;
        }

        @Override
        public void write(BinaryWriter writer) {
            writer.writeUUID(uuid);
            writer.writeVarInt(ping);
        }
    }

    public static class UpdateDisplayName extends Action<UpdateDisplayName> {
        public String displayName;

        public UpdateDisplayName() {
            super(3);
        }

        public UpdateDisplayName displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        @Override
        public void write(BinaryWriter writer) {
            writer.writeUUID(uuid);
            writer.writeBoolean(displayName != null);
            if (displayName != null) {
                writer.writeString(displayName);
            }
        }
    }

    public static class RemovePlayer extends Action<RemovePlayer> {
        public RemovePlayer() {
            super(4);
        }

        @Override
        public void write(BinaryWriter writer) {
            writer.writeUUID(uuid);
        }
    }
}
