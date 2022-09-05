/*
 * ClientboundEncryptionRequest
 *
 * 0.0.1
 *
 * 04/09/2022
 */
package fr.enimaloc.kuiper.network.packet.login;

import fr.enimaloc.kuiper.extra.MojangAuth;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import fr.enimaloc.kuiper.utils.VarIntUtils;

/**
 *
 */
public class ClientboundEncryptionRequest extends SimpleClassDescriptor implements Packet.Clientbound {

    private String serverId = "";
    private byte[] publicKey = MojangAuth.isEnabled() && MojangAuth.getKeyPair() != null
            ? MojangAuth.getKeyPair().getPublic().getEncoded()
            : null;
    private byte[] verifyToken;

    @Override
    public int id() {
        return 0x01;
    }

    public ClientboundEncryptionRequest serverId(String serverId) {
        this.serverId = serverId;
        return this;
    }

    public ClientboundEncryptionRequest publicKey(byte[] publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public ClientboundEncryptionRequest verifyToken(byte[] verifyToken) {
        this.verifyToken = verifyToken;
        return this;
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeString(this.serverId, SizedStrategy.VARINT);
        binaryWriter.writeByteArray(this.publicKey, SizedStrategy.VARINT);
        binaryWriter.writeByteArray(this.verifyToken, SizedStrategy.VARINT);
    }

    @Override
    public int length() {
        return VarIntUtils.varIntSize(this.serverId.length()) + this.serverId.length()
                + VarIntUtils.varIntSize(this.publicKey.length) + this.publicKey.length
                + VarIntUtils.varIntSize(this.verifyToken.length) + this.verifyToken.length;
    }
}
