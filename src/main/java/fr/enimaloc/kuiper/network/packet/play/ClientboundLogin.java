/*
 * ClientboundLogin
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.objects.Gamemode;
import fr.enimaloc.kuiper.objects.Identifier;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import fr.enimaloc.kuiper.world.Location;
import java.util.ArrayList;
import java.util.List;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class ClientboundLogin extends SimpleClassDescriptor implements Packet.Clientbound {

    private           int              entityId;
    private           boolean          hardcore = false;
    private           Gamemode         gamemode;
    private           Gamemode         previousGamemode;
    private           List<Identifier> dimensions;
    private           CompoundTag      registry;
    private           Identifier       dimensionType;
    private           Identifier       dimensionName;
    private           long             hashedSeed = 0;
    private           int              maxPlayers = 20;
    private           int              viewDistance = 12;
    private           int              simulationDistance = 12;
    private           boolean          reducedDebugInfo = false;
    private           boolean          enableRespawnScreen = true;
    private           boolean          debug = false;
    private           boolean          flat = false;
    @Nullable private Location         deathLocation;

    @Override
    public int id() {
        return 0x23;
    }

    public ClientboundLogin entityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public ClientboundLogin hardcore(boolean hardcore) {
        this.hardcore = hardcore;
        return this;
    }

    public ClientboundLogin gamemode(Gamemode gamemode) {
        this.gamemode = gamemode;
        return this;
    }

    public ClientboundLogin previousGamemode(Gamemode previousGamemode) {
        this.previousGamemode = previousGamemode;
        return this;
    }

    public ClientboundLogin dimensions(Identifier... dimensions) {
        return dimensions(List.of(dimensions));
    }

    public ClientboundLogin dimensions(List<Identifier> dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    public ClientboundLogin addDimension(Identifier dimension) {
        if (this.dimensions == null) {
            this.dimensions = new ArrayList<>();
        }
        this.dimensions.add(dimension);
        return this;
    }

    public ClientboundLogin registry(CompoundTag registry) {
        this.registry = registry;
        return this;
    }

    public ClientboundLogin dimensionType(Identifier dimensionType) {
        this.dimensionType = dimensionType;
        return this;
    }

    public ClientboundLogin dimensionName(Identifier dimensionName) {
        this.dimensionName = dimensionName;
        return this;
    }

    public ClientboundLogin hashedSeed(long hashedSeed) {
        this.hashedSeed = hashedSeed;
        return this;
    }

    public ClientboundLogin maxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public ClientboundLogin viewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
        return this;
    }

    public ClientboundLogin simulationDistance(int simulationDistance) {
        this.simulationDistance = simulationDistance;
        return this;
    }

    public ClientboundLogin reducedDebugInfo(boolean reducedDebugInfo) {
        this.reducedDebugInfo = reducedDebugInfo;
        return this;
    }

    public ClientboundLogin enableRespawnScreen(boolean enableRespawnScreen) {
        this.enableRespawnScreen = enableRespawnScreen;
        return this;
    }

    public ClientboundLogin debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public ClientboundLogin flat(boolean flat) {
        this.flat = flat;
        return this;
    }

    public ClientboundLogin deathLocation(@Nullable Location deathLocation) {
        this.deathLocation = deathLocation;
        return this;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeInt(this.entityId)
              .writeBoolean(this.hardcore)
              .write(this.gamemode)
              .write(this.previousGamemode == null ? Gamemode.SURVIVAL : this.previousGamemode)
              .writeList(this.dimensions, BinaryWriter::write, SizedStrategy.VARINT)
              .writeNBT(this.registry)
              .write(this.dimensionType)
              .write(this.dimensionName)
              .writeLong(this.hashedSeed)
              .writeVarInt(this.maxPlayers)
              .writeVarInt(this.viewDistance)
              .writeVarInt(this.simulationDistance)
              .writeBoolean(this.reducedDebugInfo)
              .writeBoolean(this.enableRespawnScreen)
              .writeBoolean(this.debug)
              .writeBoolean(this.flat)
              .writeBoolean(this.deathLocation != null);
        if (this.deathLocation != null) {
            writer.write(this.deathLocation);
        }
    }

}
