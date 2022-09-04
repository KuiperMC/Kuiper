/*
 * ClientboundStatusResponsePacket
 *
 * 0.0.1
 *
 * 04/09/2022
 */
package fr.enimaloc.kuiper.network.packet.status;

import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

/**
 *
 */
public class ClientboundStatusResponsePacket extends SimpleClassDescriptor implements Packet.Clientbound {

    private MinecraftVersion version = new MinecraftVersion("1.19", 759);
    private Players          players = new Players(100, 0);
    //    private ChatObject       description;
    private Favicon          favicon;

    @Override
    public int id() {
        return 0x00;
    }

    public ClientboundStatusResponsePacket version(MinecraftVersion version) {
        this.version = version;
        return this;
    }

    public ClientboundStatusResponsePacket version(String name, int protocol) {
        return this.version(new MinecraftVersion(name, protocol));
    }

    public ClientboundStatusResponsePacket players(Players players) {
        this.players = players;
        return this;
    }

    public ClientboundStatusResponsePacket players(int max, int online, Players.Player... players) {
        return this.players(new Players(max, online, players));
    }

    public ClientboundStatusResponsePacket players(int max, int online, Collection<Players.Player> players) {
        return this.players(new Players(max, online, players.toArray(Players.Player[]::new)));
    }

//    public ClientboundStatusResponsePacket description(ChatObject description) {
//        this.description = description;
//        return this;
//    }
//
//    public ClientboundStatusResponsePacket description(String text) {
//        return this.description(new ChatObject().text(text));
//    }

    public ClientboundStatusResponsePacket favicon(Favicon favicon) {
        this.favicon = favicon;
        return this;
    }

    public ClientboundStatusResponsePacket favicon(byte[] data) {
        return this.favicon(new Favicon(data));
    }

    public ClientboundStatusResponsePacket favicon(Path path) throws IOException {
        return this.favicon(new Favicon(path));
    }

    public ClientboundStatusResponsePacket favicon(URL url) throws IOException {
        return this.favicon(new Favicon(url));
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        StringJoiner json = new StringJoiner(",", "{", "}");
        json.add("\"version\":" + version)
            .add("\"players\":" + players);
//        if (description != null) {
//            json.add("\"description\":" + description);
//        }
            json.add("\"description\":{\"text\":\"Kuiper\"}");
        if (favicon != null) {
            json.add("\"favicon\":" + favicon);
        }
        binaryWriter.writeString(json.toString(), SizedStrategy.VARINT);
    }

    public record MinecraftVersion(String name, int protocolVersion) {
        @Override
        public String toString() {
            return "{\"name\": \"%s\",\"protocol\": %d}".formatted(name, protocolVersion);
        }
    }

    public record Players(int max, int online, Players.Player... sample) {

        @Override
        public String toString() {
            return "{\"max\": " + max
                   + ", \"online\": " + online
                   + (sample.length != 0
                    ? ", \"sample\": " + Arrays.stream(sample)
                                               .map(Object::toString)
                                               .collect(Collectors.joining(",", "[", "]"))
                    : "") + "}";
        }

        public record Player(String name, UUID id) {
            public Player(String name) {
                this(name, UUID.randomUUID());
            }

            @Override
            public String toString() {
                return "{\"name\": \"%s\",\"id\": \"%s\"}".formatted(name, id);
            }
        }
    }

    public static class Favicon {
        public final byte[] data;

        public Favicon(byte[] data) {
            this.data = data;
        }

        public Favicon(URL url) throws IOException {
            this(ImageIO.read(url));
        }

        public Favicon(Path path) throws IOException {
            this(ImageIO.read(path.toFile()));
        }

        public Favicon(BufferedImage bufferedImage) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, "png", outputStream);
                this.data = outputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return "\"data:image/png;base64," + Base64.getEncoder().encodeToString(this.data) + "\"";
        }

        public static class Creator {
            public static final int WIDTH  = 64;
            public static final int HEIGHT = 64;

            private BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_INDEXED);

            public Favicon.Creator fill(int color) {
                for (int x = 0; x < img.getWidth(); x++) {
                    for (int y = 0; y < img.getHeight(); y++) {
                        img.setRGB(x, y, color);
                    }
                }
                return this;
            }

            public Favicon.Creator fill(Color color) {
                return this.fill(color.getRGB());
            }

            public Favicon.Creator set(int x, int y, int color) {
                img.setRGB(x, y, color);
                return this;
            }

            public Favicon.Creator set(int x, int y, Color color) {
                return this.set(x, y, color.getRGB());
            }

            public Favicon.Creator fill(int x, int y, int width, int height, int color) {
                for (int i = x; i < x + width; i++) {
                    for (int j = y; j < y + height; j++) {
                        img.setRGB(i, j, color);
                    }
                }
                return this;
            }

            public Favicon.Creator fill(int x, int y, int width, int height, Color color) {
                return this.fill(x, y, width, height, color.getRGB());
            }

            public static Favicon.Creator random() {
                Favicon.Creator creator = new Favicon.Creator();
                for (int x = 0; x < WIDTH; x++) {
                    for (int y = 0; y < HEIGHT; y++) {
                        creator.set(x, y, new Color(
                                ThreadLocalRandom.current().nextInt(255),
                                ThreadLocalRandom.current().nextInt(255),
                                ThreadLocalRandom.current().nextInt(255)
                        ));
                    }
                }
                return creator;
            }

            public Favicon create() {
                return new Favicon(this.img);
            }
        }
    }
}
