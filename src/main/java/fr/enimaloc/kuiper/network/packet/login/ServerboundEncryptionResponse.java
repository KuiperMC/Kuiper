/*
 * ServerboundEncryptionResponse
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.enimaloc.kuiper.GameState;
import fr.enimaloc.kuiper.MinecraftServer;
import fr.enimaloc.kuiper.extra.MojangAuth;
import fr.enimaloc.kuiper.extra.mojangAuth.MojangCrypt;
import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.SecretKey;

import static fr.enimaloc.kuiper.MinecraftServer.Markers.AUTH;

/**
 *
 */
public class ServerboundEncryptionResponse extends SimpleClassDescriptor implements Packet.Serverbound {
    private static final Gson GSON = new Gson();

    public final byte[] sharedSecret;
    public final byte[] verifyToken;
    public final long   salt;
    public final byte[] signature;

    public ServerboundEncryptionResponse(BinaryReader reader) {
        this.sharedSecret = reader.readByteArray(SizedStrategy.VARINT);
        if (reader.hasNext() && reader.readBoolean()) {
            this.verifyToken = reader.readByteArray(SizedStrategy.VARINT);
            this.salt        = 0;
            this.signature   = new byte[0];
        } else {
            this.verifyToken = new byte[0];
            this.salt        = reader.readLong();
            this.signature   = reader.readByteArray(SizedStrategy.VARINT);
        }
    }

    @Override
    public int id() {
        return 0x01;
    }

    @Override
    public void handle(Connection connection) {
        if (!Arrays.equals(connection.nonce, getNonce()) && !connection.getRemoteAddress().isLoopbackAddress()) {
            MinecraftServer.LOGGER.error(AUTH, "{} tried to login with an invalid nonce!", connection.player.name());
            connection.terminate();
            return;
        }

        byte[] data = MojangCrypt.digestData("", MojangAuth.getKeyPair().getPublic(), secretKey());
        if (data == null) {
            MinecraftServer.LOGGER.error(AUTH, "Connection {} failed initializing encryption.",
                                         connection.getRemoteAddress().getHostAddress());
            connection.terminate();
            return;
        }

        String serverId = new BigInteger(data).toString(16);
        String username = URLEncoder.encode(connection.player.name(), StandardCharsets.UTF_8);

        String url = MojangAuth.AUTH_URL.formatted(username, serverId);

        HttpClient  httpClient = HttpClient.newHttpClient();
        HttpRequest request    = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).whenComplete((response, throwable) -> {
            if (throwable != null) {
                MinecraftServer.LOGGER.error(AUTH, "Failed to authenticate {} with Mojang servers.",
                                             connection.player.name());
                return;
            }

            JsonObject json = GSON.fromJson(response.body(), JsonObject.class);
            if (json == null) {
                return;
            }
            connection.encryptionKey = secretKey();
            connection.player.uuid(UUID.fromString(json.get("id")
                                                       .getAsString()
                                                       .replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                                                                     "$1-$2-$3-$4-$5")))
                             .name(json.get("name").getAsString());
            MinecraftServer.LOGGER.info(AUTH, "Player {} logged with UUID {} and authentified by Mojang server.",
                                        connection.player.name(), connection.player.uuid());
            connection.sendPacket(new ClientboundLoginSuccess().player(connection.player));
            connection.gameState = GameState.PLAY;
        });
    }

    private SecretKey secretKey() {
        return MojangCrypt.decryptByteToSecretKey(MojangAuth.getKeyPair().getPrivate(), sharedSecret);
    }

    private byte[] getNonce() {
        return MojangAuth.getKeyPair().getPrivate() == null ?
                this.verifyToken : MojangCrypt.decryptUsingKey(MojangAuth.getKeyPair().getPrivate(), this.verifyToken);
    }
}
