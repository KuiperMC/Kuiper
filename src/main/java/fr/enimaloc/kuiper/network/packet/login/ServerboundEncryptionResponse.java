/*
 * ServerboundEncryptionResponse
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.network.packet.login;

import com.google.gson.JsonObject;
import fr.enimaloc.kuiper.logger.PacketClassDescriptor;
import fr.enimaloc.kuiper.mojang.auth.MojangAuth;
import fr.enimaloc.kuiper.mojang.auth.MojangCrypt;
import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import static fr.enimaloc.kuiper.constant.Objects.GSON;

/**
 *
 */
public class ServerboundEncryptionResponse extends PacketClassDescriptor implements Packet.Serverbound {

    private byte[] sharedSecret;
    private byte[] verifyToken;

    public ServerboundEncryptionResponse(byte[] sharedSecret, byte[] verifyToken) {
        this.sharedSecret = sharedSecret;
        this.verifyToken = verifyToken;
    }

    public ServerboundEncryptionResponse(BinaryReader reader) {
        this.sharedSecret = reader.readByteArray(SizedStrategy.VARINT);
        this.verifyToken = reader.readByteArray(SizedStrategy.VARINT);
    }

    @Override
    public int getPacketId() {
        return 0x01;
    }

    @Override
    public void handle(Connection connection) {
        byte[] token = MojangCrypt.decryptUsingKey(MojangAuth.getKeyPair().getPrivate(), verifyToken);
        byte[] nonce = (byte[]) connection.data.get("nonce");
        if (!Arrays.equals(token, nonce)) {
            Connection.LOGGER.warn("{} tried to connect with an invalid token", connection.player.getProfile().username);
            connection.terminate("Invalid token");
            return;
        }
        byte[] digestedData = MojangCrypt.digestData("", MojangAuth.getKeyPair().getPublic(), getSecretKey());
        if (digestedData == null) {
            Connection.LOGGER.warn("{} failed initializing encryption.", connection.player.getProfile().username);
            connection.terminate("Encryption initialization failed");
            return;
        }

        String serverId = new BigInteger(digestedData).toString(16);
        String username = URLEncoder.encode(connection.player.getProfile().username, StandardCharsets.UTF_8);
        String url      = MojangAuth.AUTH_URL.formatted(username, serverId);

        HttpClient  client  = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, throwable) -> {
                    if (throwable != null) {
                        Connection.LOGGER.warn("{} failed to authenticate with Mojang servers.", connection.player.getProfile().username);
                        connection.terminate("Authentication failed");
                        return;
                    }
                    JsonObject gameProfile = GSON.fromJson(response.body(), JsonObject.class);
                    if (gameProfile == null) {
                        Connection.LOGGER.warn("{} failed to authenticate with Mojang servers.", connection.player.getProfile().username);
                        connection.terminate("Authentication failed");
                        return;
                    }
                    connection.data.put("secret", getSecretKey());
                    connection.player.getProfile().uuid = UUID.fromString(gameProfile.get("id").getAsString().replaceFirst(
                            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                            "$1-$2-$3-$4-$5"
                    ));
                    connection.player.getProfile().username = gameProfile.get("name").getAsString();
                    connection.startPlayState();
                });
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeByteArray(sharedSecret, SizedStrategy.VARINT);
        binaryWriter.writeByteArray(verifyToken, SizedStrategy.VARINT);
    }

    private SecretKey getSecretKey() {
        return MojangCrypt.decryptByteToSecretKey(MojangAuth.getKeyPair().getPrivate(), sharedSecret);
    }
}
