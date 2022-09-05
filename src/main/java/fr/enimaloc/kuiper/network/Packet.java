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
import fr.enimaloc.kuiper.network.packet.login.*;
import fr.enimaloc.kuiper.network.packet.play.*;
import fr.enimaloc.kuiper.network.packet.status.*;
import fr.enimaloc.kuiper.network.packet.unknown.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public interface Packet {

    int id();

    public interface Clientbound extends Packet, Writeable {
    }

    public interface Serverbound extends Packet {

        static PacketList get(int id, GameState gameState) {
            return PacketList.filter(packet -> packet.id == id
                                               && packet.type == Type.SERVERBOUND
                                               && packet.state == gameState)
                             .orElseThrow(() -> new PacketNotFoundException(id, Type.SERVERBOUND, gameState));
        }

        void handle(Connection connection);
    }

    enum PacketList {
        HANDSHAKE(Type.SERVERBOUND, 0x00, GameState.UNKNOWN, ServerboundHandshake::new),

        SERVERBOUND_STATUS_REQUEST(Type.SERVERBOUND, 0x00, GameState.STATUS, ServerboundStatusRequest::new),
        CLIENTBOUND_STATUS_RESPONSE(Type.CLIENTBOUND, 0x00, GameState.STATUS),
        SERVERBOUND_PING_REQUEST(Type.SERVERBOUND, 0x01, GameState.STATUS, ServerboundPingRequest::new),
        CLIENTBOUND_PONG_RESPONSE(Type.CLIENTBOUND, 0x01, GameState.STATUS),

        SERVERBOUND_LOGIN_START(Type.SERVERBOUND, 0x00, GameState.LOGIN, ServerboundLoginStart::new),
        CLIENTBOUND_ENCRYPTION_REQUEST(Type.CLIENTBOUND, 0x01, GameState.LOGIN),
        SERVERBOUND_ENCRYPTION_RESPONSE(Type.SERVERBOUND, 0x01, GameState.LOGIN, ServerboundEncryptionResponse::new),
        CLIENTBOUND_SET_COMPRESSION(Type.CLIENTBOUND, 0x03, GameState.LOGIN),
        CLIENTBOUND_LOGIN_SUCCESS(Type.CLIENTBOUND, 0x02, GameState.LOGIN),

        SERVERBOUND_CLIENT_INFORMATION(Type.SERVERBOUND, 0x07, GameState.PLAY, ServerboundClientInformation::new),
        CLIENTBOUND_CHANGE_DIFFICULTY(Type.CLIENTBOUND, 0x0B, GameState.PLAY),
        SERVERBOUND_CUSTOM_PAYLOAD(Type.SERVERBOUND, 0x0C, GameState.PLAY, ServerboundCustomPayload::new),
        CLIENTBOUND_CUSTOM_PAYLOAD(Type.CLIENTBOUND, 0x15, GameState.PLAY),
        CLIENTBOUND_ENTITY_EVENT(Type.CLIENTBOUND, 0x18, GameState.PLAY),
        CLIENTBOUND_LOGIN(Type.CLIENTBOUND, 0x23, GameState.PLAY),
        SERVERBOUND_SET_CARRIED_ITEM(Type.SERVERBOUND, 0x27, GameState.PLAY, ServerboundSetCarriedItem::new),
        CLIENTBOUND_PLAYER_ABILITIES(Type.CLIENTBOUND, 0x2F, GameState.PLAY),
        CLIENTBOUND_SET_CARRIED_ITEM(Type.CLIENTBOUND, 0x47, GameState.PLAY),
        CLIENTBOUND_UPDATE_RECIPES(Type.CLIENTBOUND, 0x67, GameState.PLAY),
        CLIENTBOUND_UPDATE_TAGS(Type.CLIENTBOUND, 0x68, GameState.PLAY),
        ;

        public final GameState                                state;
        public final Type                                     type;
        public final int                                      id;
        @Nullable
        public final Function<BinaryReader, ? extends Packet> packetBuilder;

        PacketList(
                Type type, int id, GameState state, @Nullable Function<BinaryReader, ? extends Packet> packetBuilder
        ) {
            this.type          = type;
            this.id            = id;
            this.state         = state;
            this.packetBuilder = packetBuilder;
        }

        PacketList(int id, GameState state, @Nullable Function<BinaryReader, ? extends Packet> packetBuilder) {
            this(packetBuilder == null ? Type.CLIENTBOUND : Type.SERVERBOUND, id, state, packetBuilder);
        }

        PacketList(Type type, int id, GameState state) {
            this(type, id, state, null);
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
    }

    enum Type {
        CLIENTBOUND,
        SERVERBOUND
    }
}
