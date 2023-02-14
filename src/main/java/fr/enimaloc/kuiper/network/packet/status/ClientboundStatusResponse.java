/*
 * ClientboundStatusResponse
 *
 * 0.0.1
 *
 * 09/02/2023
 */
package fr.enimaloc.kuiper.network.packet.status;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fr.enimaloc.kuiper.constant.Constant;
import fr.enimaloc.kuiper.logger.PacketClassDescriptor;
import fr.enimaloc.kuiper.logger.SimpleClassDescriptor;
import fr.enimaloc.kuiper.mojang.ChatObject;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.object.BufferedImageCreator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

/**
 *
 */
public class ClientboundStatusResponse extends PacketClassDescriptor implements Packet.Clientbound {

    @Expose
    private MinecraftVersion version           = new MinecraftVersion();
    @Expose
    private Players          players           = new Players(20);
    @Expose
    @SerializedName("description")
    private ChatObject       motw              = new ChatObject("Kuiper");
    @Expose
    private Favicon          favicon;
    @Expose
    private boolean          previewChat       = true;
    @Expose
    private boolean          enforceSecureChat = true;

    public ClientboundStatusResponse(BinaryReader reader) {
        ClientboundStatusResponse response = new Gson().fromJson(reader.readString(SizedStrategy.VARINT), ClientboundStatusResponse.class);
        this.version = response.version;
        this.players = response.players;
        this.motw = response.motw;
        this.favicon = response.favicon;
        this.previewChat = response.previewChat;
        this.enforceSecureChat = response.enforceSecureChat;
    }

    public ClientboundStatusResponse() {
        try {
            this.favicon = new Favicon(
                    BufferedImageCreator.fromImage(new URL("https://lh3.googleusercontent.com/ogw/AAEL6sh6TvDFnVgERi85m0PbBOshRiHeQbKIXPETUeeKntg=s32-c-mo"), Favicon.IMAGE_TYPE)
                            .resize(64, 64)
                            .text(0, 12, 38, "Kuiper", Color.BLUE, new Font("Arial", Font.BOLD, 23))
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public ClientboundStatusResponse setVersion(MinecraftVersion version) {
        this.version = version;
        return this;
    }

    public ClientboundStatusResponse setVersion(String name, int protocol) {
        return setVersion(new MinecraftVersion(name, protocol));
    }

    public ClientboundStatusResponse setPlayers(Players players) {
        this.players = players;
        if (players.online == null) {
            this.players.online = players.sample.size();
        }
        return this;
    }

    public ClientboundStatusResponse setPlayers(int max, int online, List<Players.Player> sample) {
        return setPlayers(new Players(max, online, sample));
    }

    public ClientboundStatusResponse setPlayers(int max, int online, Players.Player... sample) {
        return setPlayers(max, online, List.of(sample));
    }

    public ClientboundStatusResponse setPlayers(int max, List<Players.Player> sample) {
        return setPlayers(max, sample.size(), sample);
    }

    public ClientboundStatusResponse setPlayers(int max, Players.Player... sample) {
        return setPlayers(max, List.of(sample));
    }

    public ClientboundStatusResponse setPlayers(int max) {
        return setPlayers(new Players(max));
    }

    public ClientboundStatusResponse setPlayers(int max, int online, Collection<String> sample) {
        return setPlayers(max, online, sample.stream().map(Players.Player::new).toList());
    }

    public ClientboundStatusResponse setPlayers(int max, Collection<String> sample) {
        return setPlayers(max, sample.stream().map(Players.Player::new).toList());
    }

    public ClientboundStatusResponse setPlayers(int max, int online, String... sample) {
        return setPlayers(max, online, List.of(sample));
    }

    public ClientboundStatusResponse setPlayers(int max, String... sample) {
        return setPlayers(max, List.of(sample));
    }

    public ClientboundStatusResponse setMotw(ChatObject motw) {
        this.motw = motw;
        return this;
    }

    public ClientboundStatusResponse setMotw(String motw) {
        return setMotw(new ChatObject(motw));
    }

    public MinecraftVersion getVersion() {
        return version;
    }

    public Players getPlayers() {
        return players;
    }

    public ChatObject getMotw() {
        return motw;
    }

    public Favicon getFavicon() {
        return favicon;
    }

    @Override
    public int getPacketId() {
        return 0x00;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeString(buildJSON(), SizedStrategy.VARINT);
    }

    @Override
    protected Map<String, String> lastAdditionalFields() {
        return Map.of("json", buildJSON());
    }

    private String buildJSON() {
        if (players.online == null) {
            players.online = players.sample.size();
        }
        return new Gson().toJson(this);
    }

    public static class MinecraftVersion {
        @Expose
        private String name;
        @Expose
        private int    protocol;

        public MinecraftVersion() {
            this(Constant.MINECRAFT_VERSION_NAME, Constant.PROTOCOL_VERSION);
        }

        public MinecraftVersion(String name, int protocol) {
            this.name = name;
            this.protocol = protocol;
        }

        public String getName() {
            return name;
        }

        public int getProtocol() {
            return protocol;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (MinecraftVersion) obj;
            return Objects.equals(this.name, that.name) &&
                    this.protocol == that.protocol;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, protocol);
        }

        @Override
        public String toString() {
            return "MinecraftVersion[" +
                    "name=" + name + ", " +
                    "protocol=" + protocol + ']';
        }

    }

    public static class Players extends SimpleClassDescriptor {
        @Expose
        private int          max;
        @Expose
        private Integer      online = null;
        @Expose
        private List<Player> sample;

        public Players(int max, Integer online, List<Player> sample) {
            this.max = max;
            this.online = online;
            this.sample = sample;
        }

        public Players(int max, List<Player> sample) {
            this.max = max;
            this.sample = sample;
        }

        public Players(int max, Integer online, Player... sample) {
            this.max = max;
            this.online = online;
            this.sample = List.of(sample);
        }

        public Players(int max, Player... sample) {
            this.max = max;
            this.sample = List.of(sample);
        }

        public static final class Player {
            @Expose
            private String name;
            @Expose
            private UUID   id;

            public Player(String name, UUID id) {
                this.name = name;
                this.id = id;
            }

            public Player(String name) {
                this(name, UUID.randomUUID());
            }

            public String getName() {
                return name;
            }

            public UUID getID() {
                return id;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == this) return true;
                if (obj == null || obj.getClass() != this.getClass()) return false;
                var that = (Player) obj;
                return Objects.equals(this.name, that.name) &&
                        Objects.equals(this.id, that.id);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, id);
            }

            @Override
            public String toString() {
                return "Player[" +
                        "name=" + name + ", " +
                        "id=" + id + ']';
            }
        }
    }

    public static class Favicon {

        public static final int WIDTH = 64;
        public static final int HEIGHT     = 64;
        public static final int IMAGE_TYPE = BufferedImage.TYPE_BYTE_INDEXED;

        @Expose
        private String base64;

        public Favicon(BufferedImageCreator creator) throws IOException {
            this(creator.create());
        }

        public Favicon(URL url) throws IOException {
            this(url, "png");
        }

        public Favicon(URL url, String format) throws IOException {
            this(ImageIO.read(url), format);
        }

        public Favicon(Path path) throws IOException {
            this(ImageIO.read(path.toFile()), Optional.of(path)
                    .map(Path::toString)
                    .filter(p -> p.contains("."))
                    .map(p -> p.substring(p.lastIndexOf(".") + 1))
                    .orElse("png"));
        }

        public Favicon(Path path, String format) throws IOException {
            this(ImageIO.read(path.toFile()), format);
        }

        public Favicon(BufferedImage image) throws IOException {
            this(image, "png");
        }

        public Favicon(BufferedImage image, String format) throws IOException {
            if (image.getWidth() != WIDTH || image.getHeight() != HEIGHT) {
                throw new IllegalArgumentException("Image must be " + WIDTH + "x" + HEIGHT + " pixels");
            }
            if (image.getType() != IMAGE_TYPE) {
                throw new IllegalArgumentException("Image must be of type " + IMAGE_TYPE);
            }
            if (format == null || format.isEmpty()) {
                format = "png";
            }
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(image, format, baos);
                baos.flush();
                this.base64 = "data:image/" + format + ";base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
            }
        }

        public Favicon(String base64) {
            this.base64 = base64;
        }

        public String getBase64() {
            return base64;
        }

        public static BufferedImageCreator createImage() {
            return BufferedImageCreator.fromNothing(WIDTH, HEIGHT, IMAGE_TYPE);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Favicon) obj;
            return Objects.equals(this.base64, that.base64);
        }

        @Override
        public int hashCode() {
            return Objects.hash(base64);
        }

        @Override
        public String toString() {
            return "Favicon[" +
                    "base64=" + base64 + ']';
        }
    }
}
