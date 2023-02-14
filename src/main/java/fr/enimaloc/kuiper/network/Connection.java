/*
 * Connection
 *
 * 0.0.1
 *
 * 02/09/2022
 */
package fr.enimaloc.kuiper.network;

import ch.qos.logback.classic.Logger;
import fr.enimaloc.kuiper.GameState;
import fr.enimaloc.kuiper.MinecraftServer;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.packet.login.ClientboundLoginSuccess;
import fr.enimaloc.kuiper.network.packet.unknown.ServerboundHandshake;
import fr.enimaloc.kuiper.utils.VarIntUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import static fr.enimaloc.kuiper.MinecraftServer.Markers.*;

/**
 *
 */
public class Connection implements Runnable {

    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(Connection.class);
    private static final ExecutorService POOL   = Executors.newFixedThreadPool(10);

    @NotNull
    public final  MinecraftServer     server;
    public final  Map<String, Object> data             = new HashMap<>();
    @NotNull
    private final Socket              socket;
    @NotNull
    private final List<Packet> exchangedPackets = new ArrayList<>();
    public        GameState    gameState        = GameState.UNKNOWN;
    @Nullable
    private       Packet       lastReceived;

    public Connection(@NotNull Socket socket, @NotNull MinecraftServer server) {
        this.server = server;
        LOGGER.debug(NETWORK, "New connection from {}", socket.getInetAddress().getHostAddress());
        this.socket = socket;
    }

    public Connection(Object... args) {
        this((Socket) args[0], (MinecraftServer) args[1]);
    }

    @Override
    public void run() {
        InputStream inputStream;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        while (!socket.isClosed()) {
            try {
                VarIntUtils.VarInt lenVarInt = VarIntUtils.readVarInt(inputStream);
                if (lenVarInt == null) {
                    lastReceived = null;
                    continue;
                }
                try (BinaryReader reader
                             = new BinaryReader(ByteBuffer.allocate(lenVarInt.value() + lenVarInt.length())
                                                          .put(VarIntUtils.getVarInt(lenVarInt.value()))
                                                          .put(inputStream.readNBytes(lenVarInt.value()))
                                                          .rewind())) {

                    reader.readVarInt(); // Remove length var int from buffer
                    int                packetId = reader.readVarInt();
                    Packet.Serverbound packet   = Packet.Serverbound.get(packetId, gameState).build(reader);
                    LOGGER.makeLoggingEventBuilder(Level.DEBUG)
                          .addMarker(NETWORK)
                          .addMarker(NETWORK_IN)
                          .log("{} -> SERVER: {}", socket.getInetAddress().getHostAddress(), packet);

                    if (LOGGER.isTraceEnabled()) {
                        AtomicInteger i = new AtomicInteger(0);
                        LOGGER.makeLoggingEventBuilder(Level.TRACE)
                              .addMarker(NETWORK)
                              .addMarker(NETWORK_IN)
                              .log("{} => SERVER: {}",
                                   socket.getInetAddress().getHostAddress(),
                                   Stream.generate(() -> reader.getBuffer().array()[i.getAndIncrement()])
                                         .limit(reader.getBuffer().limit())
                                         .map(j -> String.format("%02x", j))
                                         .collect(Collectors.joining(" ")));
                    }

                    if (packet instanceof ServerboundHandshake) {
                        packet.handle(this);
                    } else {
                        POOL.submit(() -> packet.handle(this));
                    }
                    exchangedPackets.add(packet);
                    lastReceived = packet;
                }
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    LOGGER.makeLoggingEventBuilder(Level.ERROR)
                            .addMarker(NETWORK)
                            .addMarker(NETWORK_IN)
                            .log("Error while reading packet from {}", socket.getInetAddress().getHostAddress(), e);
                }
            }
        }
    }

    public void sendPacket(Packet.Clientbound packet) {
        exchangedPackets.add(packet);
        LOGGER.makeLoggingEventBuilder(Level.DEBUG)
              .addMarker(NETWORK)
              .addMarker(NETWORK_OUT)
              .log("SERVER -> {}: {}", socket.getInetAddress().getHostAddress(), packet);
        try (BinaryWriter writer = new BinaryWriter(0).writeVarInt(packet.getPacketId())
                                                      .write(packet)
                                                      .trim()
        ) {
            byte[] varInt = VarIntUtils.getVarInt(writer.getBuffer().array().length);
            byte[] bytes  = new byte[writer.getBuffer().array().length + varInt.length];
            System.arraycopy(varInt, 0, bytes, 0, varInt.length);
            System.arraycopy(writer.getBuffer().array(), 0, bytes, varInt.length, writer.getBuffer().array().length);

            if (LOGGER.isTraceEnabled()) {
                AtomicInteger i = new AtomicInteger(0);
                LOGGER.makeLoggingEventBuilder(Level.TRACE)
                      .addMarker(NETWORK)
                      .addMarker(NETWORK_OUT)
                      .log("SERVER => {}: {}",
                           socket.getInetAddress().getHostAddress(),
                           Stream.generate(() -> bytes[i.getAndIncrement()])
                                 .limit(bytes.length)
                                 .map(j -> String.format("%02x", j))
                                 .collect(Collectors.joining(" ")));
            }

            socket.getOutputStream().write(bytes);
            socket.getOutputStream().flush();

            if (packet instanceof ClientboundDisconnectLogin) {
                terminate();
            }
        } catch (IOException e) {
            terminate(e);
        }
    }

    public void terminate(Throwable e) {
        LOGGER.error(NETWORK, "Connection terminated", e);
        switch (gameState) {
            case LOGIN:
                sendPacket(new ClientboundDisconnectLogin(new ChatObject(e.toString())));
                break;
            case PLAY:
                // TODO: 14/02/2023 Send disconnect packet
                break;
        }
    }

    public void terminate(String reason) {
        LOGGER.debug(NETWORK, "Connection terminated: {}", reason);
        switch (gameState) {
            case LOGIN:
                sendPacket(new ClientboundDisconnectLogin(new ChatObject(reason)));
                break;
            case PLAY:
                // TODO: 14/02/2023 Send disconnect packet
                break;
        }
    }

    public void terminate() {
        try {
            socket.close();
        } catch (IOException e) {
            LOGGER.error(NETWORK, "Error while closing socket", e);
        }
    }

    public void startPlayState() {
        LOGGER.info("UUID of player {} is {}.", player.getProfile().username, player.getProfile().uuid);
        sendPacket(new ClientboundLoginSuccess(player.getProfile().uuid, player.getProfile().username));
        gameState = GameState.PLAY;
    }
}
