/*
 * Connection
 *
 * 0.0.1
 *
 * 02/09/2022
 */
package fr.enimaloc.kuiper.network;

import ch.qos.logback.classic.Logger;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import static fr.enimaloc.kuiper.MinecraftServer.Markers.*;

/**
 *
 */
public class Connection extends Thread {

    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(Connection.class);

    @NotNull private final Socket socket;
    @NotNull private final List<Packet> exchangedPackets = new ArrayList<>();
    @Nullable private Packet lastReceived;

    public Connection(@NotNull Socket socket) {
        LOGGER.debug(NETWORK, "New connection from {}", socket.getInetAddress().getHostAddress());
        this.socket = socket;
    }

    public Connection(Object... args) {
        this((Socket) args[0]);
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
//            VarIntUtils.VarInt lenVarInt = VarIntUtils.readVarInt(inputStream);
//            if (lenVarInt == null) {
//                return null;
//            }
//
//            int length = lenVarInt.value();
//            return ByteBuffer.allocate(length + lenVarInt.length())
//                             .put(VarIntUtils.getVarInt(length))
//                             .put(inputStream.readNBytes(length));
            Packet.Serverbound packet = null;

            exchangedPackets.add(packet);
            packet.handle(this);
        }
    }

    public void sendPacket(Packet.Clientbound packet) {
        exchangedPackets.add(packet);
        LOGGER.makeLoggingEventBuilder(Level.DEBUG)
              .addMarker(NETWORK)
              .addMarker(NETWORK_OUT)
              .log("-> {}: {}", socket.getInetAddress().getHostAddress(), packet);
    }

    public void terminate(Throwable e) {
        LOGGER.error(NETWORK, "Connection terminated", e);
        try {
            socket.close();
        } catch (IOException ioException) {
            LOGGER.error(NETWORK, "Error while closing socket", ioException);
        }
    }

    public void terminate(String reason) {
        LOGGER.debug(NETWORK, "Connection terminated: {}", reason);
        try {
            socket.close();
        } catch (IOException e) {
            LOGGER.error(NETWORK, "Error while closing socket", e);
        }
    }
}
