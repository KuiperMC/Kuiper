/*
 * ClientboundEncryptionRequest
 *
 * 0.0.1
 *
 * 14/02/2023
 */
package fr.enimaloc.kuiper.network.packet.login;

import fr.enimaloc.kuiper.logger.SimpleClassDescriptor;
import fr.enimaloc.kuiper.mojang.auth.MojangAuth;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;

/**
 *
 */
public class ClientboundEncryptionRequest extends SimpleClassDescriptor implements Packet.Clientbound {

    private String serverId;
    private byte[] publicKey;
    private byte[] verifyToken;

    public ClientboundEncryptionRequest(String serverId, byte[] publicKey, byte[] verifyToken) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    public ClientboundEncryptionRequest(byte[] verifyToken) {
        this(
                "",
                MojangAuth.isEnabled() && MojangAuth.getKeyPair() != null
                        ? MojangAuth.getKeyPair().getPublic().getEncoded()
                        : new byte[0],
                verifyToken
        );
    }

    public ClientboundEncryptionRequest(BinaryReader reader) {
        this.serverId = reader.readString(SizedStrategy.VARINT);
        this.publicKey = reader.readByteArray(SizedStrategy.VARINT);
        this.verifyToken = reader.readByteArray(SizedStrategy.VARINT);
    }

    @Override
    public int getPacketId() {
        return 0x01;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeString(serverId, SizedStrategy.VARINT);
        binaryWriter.writeByteArray(publicKey, SizedStrategy.VARINT);
        binaryWriter.writeByteArray(verifyToken, SizedStrategy.VARINT);
    }
}
