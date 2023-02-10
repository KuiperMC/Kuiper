/*
 * StatusTest
 *
 * 0.0.1
 *
 * 09/02/2023
 */
package fr.enimaloc.kuiper.protocol;

import fr.enimaloc.kuiper.GameState;
import fr.enimaloc.kuiper.MinecraftServer;
import fr.enimaloc.kuiper.data.ServerSettings;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.network.packet.status.ClientboundPingResponse;
import fr.enimaloc.kuiper.network.packet.status.ClientboundStatusResponse;
import fr.enimaloc.kuiper.network.packet.status.ServerboundPingRequest;
import fr.enimaloc.kuiper.network.packet.status.ServerboundStatusRequest;
import fr.enimaloc.kuiper.network.packet.unknown.ServerboundHandshake;
import fr.enimaloc.kuiper.utils.VarIntUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.BindException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 *
 */
public class StatusTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(StatusTest.class);

    private MinecraftServer kuiper;
    private Connection          client;

    @BeforeEach
    public void init() {
        ServerSettings settings = new ServerSettings();
        while (kuiper == null) {
            try {
                kuiper = new MinecraftServer(settings);
            } catch (BindException e) {
                LOGGER.warn("Port already in use, retrying with another one");
                settings.port++;
                kuiper = null;
            } catch (IOException e) {
                abort(e.toString());
            }
        }
        LOGGER.info("Initialising Socket");
        try {
            client = new Connection(new Socket("127.0.0.1", kuiper.getSettings().port));
        } catch (IOException e) {
            abort(e.toString());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            client.socket().close();
            kuiper.shutdown();
        } catch (IOException e) {
            abort(e.toString());
        }
    }

    @Test
    void statusRequest() {
        client.send(new ServerboundHandshake().setProtocolVersion(172)
                .setServerAddress("test.localhost")
                .setServerPort(kuiper.getSettings().port)
                .setState(GameState.STATUS));
        client.send(new ServerboundStatusRequest());
        Connection.PartialPacket     partialPacket = client.awaitPacket();
        Optional<Packet.Clientbound> packetOpt     = partialPacket.getPacketClientbound(GameState.STATUS);
        assertTrue(packetOpt.isPresent());
        assertInstanceOf(ClientboundStatusResponse.class, packetOpt.get());
        ClientboundStatusResponse packet = (ClientboundStatusResponse) packetOpt.get();
        assertEquals(0x00, packet.getPacketId());
        assertEquals(759, packet.getVersion().getProtocol());
    }

    @Test
    void pingRequest() {
        client.send(new ServerboundHandshake().setProtocolVersion(172)
                .setServerAddress("test.localhost")
                .setServerPort(kuiper.getSettings().port)
                .setState(GameState.STATUS));
        client.send(new ServerboundStatusRequest());
        assumeTrue(client.awaitPacket().getPacketClientbound(GameState.STATUS).isPresent());
        long payload = System.currentTimeMillis();
        client.send(new ServerboundPingRequest(payload));
        Connection.PartialPacket     partialPacket = client.awaitPacket();
        Optional<Packet.Clientbound> packetOpt     = partialPacket.getPacketClientbound(GameState.STATUS);
        assertTrue(packetOpt.isPresent());
        assertInstanceOf(ClientboundPingResponse.class, packetOpt.get());
        assertEquals(payload, ((ClientboundPingResponse) packetOpt.get()).getPayload());
    }

    record Connection(Socket socket, InputStream input, PrintStream output) {
        public Connection(Socket socket) throws IOException{
            this(socket, socket.getInputStream(), new PrintStream(socket.getOutputStream()));
        }

        public void send(Packet.Serverbound packet) {
            BinaryWriter writer = new BinaryWriter();
            writer.writeVarInt(packet.getPacketId());
            writer.write(packet);
            writer = new BinaryWriter().writeByteArray(writer.toByteArray(), SizedStrategy.VARINT);
            try {
                output.write(writer.toByteArray());
                writer.close();
            } catch (IOException e) {
                fail(e);
            }
        }

        public PartialPacket awaitPacket() {
            return awaitPacket(null);
        }

        public PartialPacket awaitPacket(Supplier<Packet.Serverbound> inReplyOf) {
            while (true) {
                if (inReplyOf != null) {
                    send(inReplyOf.get());
                    inReplyOf = null;
                }
                try {
                    VarIntUtils.VarInt lenVarInt = VarIntUtils.readVarInt(input);
                    if (lenVarInt == null) {
                        continue;
                    }
                    try (BinaryReader reader
                                 = new BinaryReader(ByteBuffer.allocate(lenVarInt.value() + lenVarInt.length())
                            .put(VarIntUtils.getVarInt(lenVarInt.value()))
                            .put(input.readNBytes(lenVarInt.value())).rewind())) {
                        int                length   = reader.readVarInt();
                        int                packetId = reader.readVarInt();
                        return new PartialPacket(length, packetId, reader);
                    }
                } catch (IOException e) {
                    fail(e);
                }
            }
        }

        public record PartialPacket(int length, int id, BinaryReader reader) {
            public Optional<Packet.Clientbound> getPacketClientbound(GameState state) {
                return Packet.PacketList.filter(p -> p.type == Packet.Type.CLIENTBOUND && p.id == id && p.state == state)
                        .map(p -> p.packetBuilder.apply(reader))
                        .map(Packet.Clientbound.class::cast);
            }
        }
    }

    protected boolean waitFor(int timeout, TimeUnit unit) {
        return !waitFor(() -> false, timeout, unit);
    }

    protected static boolean waitFor(BooleanSupplier condition) {
        return waitFor(condition, true);
    }

    protected static boolean waitFor(BooleanSupplier condition, boolean expected) {
        return waitFor(condition, expected, 5, TimeUnit.SECONDS);
    }

    protected static boolean waitFor(BooleanSupplier condition, int timeout, TimeUnit unit) {
        return waitFor(condition, true, timeout, unit);
    }

    protected static boolean waitFor(BooleanSupplier condition, boolean expected, int timeout, TimeUnit unit) {
        long timedOut = System.currentTimeMillis() + unit.toMillis(timeout);
        while (condition.getAsBoolean() != expected) {
            if (System.currentTimeMillis() >= timedOut) {
                return false;
            }
        }
        return true;
    }
}
