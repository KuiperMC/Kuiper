/*
 * ClientboundPlayerAbilities
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import java.util.BitSet;

/**
 *
 */
public class ClientboundPlayerAbilities extends SimpleClassDescriptor implements Packet.Clientbound {
    public static final byte INVULNERABLE = 0x01;
    public static final byte FLYING = 0x02;
    public static final byte ALLOW_FLYING = 0x04;
    public static final byte INSTANT_BREAK = 0x08;

    private boolean invulnerable;
    private boolean flying;
    private boolean allowFlying;
    private boolean creativeMode;
    private float  flyingSpeed = 0.05f;
    private float  fovModifier = 0.1f;

    @Override
    public int id() {
        return 0x2F;
    }

    public ClientboundPlayerAbilities invulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
        return this;
    }

    // FIXME: 05/09/2022 When active player stuck in "Loading terrain..." screen
    //  fix for removing @Deprecated
    @Deprecated
    public ClientboundPlayerAbilities flying(boolean flying) {
        this.flying = flying;
        return this;
    }

    public ClientboundPlayerAbilities allowFlying(boolean allowFlying) {
        this.allowFlying = allowFlying;
        return this;
    }

    public ClientboundPlayerAbilities creativeMode(boolean creativeMode) {
        this.creativeMode = creativeMode;
        return this;
    }

    public ClientboundPlayerAbilities flyingSpeed(float flyingSpeed) {
        this.flyingSpeed = flyingSpeed;
        return this;
    }

    public ClientboundPlayerAbilities fovModifier(float fovModifier) {
        this.fovModifier = fovModifier;
        return this;
    }

    public byte flags() {
        return (byte) ((invulnerable ? INVULNERABLE : 0x00)
                       | (flying ? FLYING : 0x00)
                       | (allowFlying ? ALLOW_FLYING : 0x00)
                       | (creativeMode ? INSTANT_BREAK : 0x00));
    }

    @Override
    public void write(BinaryWriter binaryWriter) {
        binaryWriter.writeByte(flags());
        binaryWriter.writeFloat(flyingSpeed);
        binaryWriter.writeFloat(fovModifier);
    }
}
