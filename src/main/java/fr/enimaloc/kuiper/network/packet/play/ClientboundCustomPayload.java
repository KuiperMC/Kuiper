/*
 * ClientboundPluginMessage
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.objects.Identifier;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;

/**
 *
 */
public class ClientboundCustomPayload extends SimpleClassDescriptor implements Packet.Clientbound {

    private Identifier channel;
    private byte[] data;

    public static ClientboundCustomPayload brand() {
        return new ClientboundCustomPayload().channel(Identifier.minecraft("brand"));
    }

    @Override
    public int id() {
        return 0x15;
    }

    public ClientboundCustomPayload channel(Identifier channel) {
        this.channel = channel;
        return this;
    }

    public ClientboundCustomPayload data(byte[] data) {
        this.data = data;
        return this;
    }

    public ClientboundCustomPayload data(String message) {
        return data(message.getBytes());
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.write(channel);
        binaryWriter.writeByteArray(data, SizedStrategy.VARINT);
    }
}
