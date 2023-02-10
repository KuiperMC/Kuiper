/*
 * Packet
 *
 * 0.0.1
 *
 * 02/09/2022
 */
package fr.enimaloc.kuiper.network;

import fr.enimaloc.kuiper.GameState;
import fr.enimaloc.kuiper.exception.PacketNotFoundException;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.Writeable;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import fr.enimaloc.kuiper.network.packet.unknown.ServerboundHandshake;
/**
 *
 */
public interface Packet extends Writeable {

    int getPacketId();

    interface Clientbound extends Packet, Writeable {
    }

    interface Serverbound extends Packet {

        static PacketList get(int id, GameState gameState) {
            return PacketList.filter(packet -> packet.id == id
                                               && packet.type == Type.SERVERBOUND
                                               && packet.state == gameState)
                             .orElseThrow(() -> new PacketNotFoundException(id, Type.SERVERBOUND, gameState));
        }

        void handle(Connection connection);
    }

    enum PacketList {
        SERVERBOUND_HANDSHAKE(0x00, GameState.UNKNOWN, ServerboundHandshake.class, ServerboundHandshake::new),
        ;

        public final GameState                                state;
        public final Type                                     type;
        public final int                                      id;
        public final Class<? extends Packet> clazz;
        public final Function<BinaryReader, ? extends Packet> packetBuilder;

        PacketList(
                Type type, int id, GameState state, Class<? extends Packet> clazz, Function<BinaryReader, ? extends Packet> packetBuilder
        ) {
            this.type          = type;
            this.id            = id;
            this.state         = state;
            this.clazz = clazz;
            this.packetBuilder = packetBuilder;
        }

        PacketList(int id, GameState state, Class<? extends Packet> clazz, Function<BinaryReader, ? extends Packet> packetBuilder) {
            this(Clientbound.class.isAssignableFrom(clazz) ? Type.CLIENTBOUND : Type.SERVERBOUND, id, state, clazz, packetBuilder);
        }

        PacketList(Type type, int id, GameState state, Class<? extends Packet> clazz) {
            this(type, id, state, clazz, null);
        }

        public static Optional<PacketList> filter(Predicate<PacketList> filter) {
            return Arrays.stream(PacketList.values()).filter(filter).findFirst();
        }

        public Serverbound build(BinaryReader reader) {
            if (type == Type.CLIENTBOUND || packetBuilder == null) {
                throw new IllegalStateException("Cannot build a clientbound packet");
            }
            return (Serverbound) packetBuilder.apply(reader);
        }

        @Override
        public String toString() {
            return "PacketList{" +
                    "state=" + state +
                    ", type=" + type +
                    ", id=" + id +
                    ", clazz=" + clazz +
                    '}';
        }
    }

    enum Type {
        CLIENTBOUND,
        SERVERBOUND
    }
}
