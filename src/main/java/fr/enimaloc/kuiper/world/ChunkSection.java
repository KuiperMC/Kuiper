/*
 * ChunkSection
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.world;

import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.Writeable;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import fr.enimaloc.kuiper.world.palette.Palette;
import java.util.Objects;

/**
 *
 */
public class ChunkSection extends SimpleClassDescriptor implements Writeable {
    private final int     x;
    private final int     y;
    private final int     z;
    private       Palette blockPalette;
    private       Palette biomePalette;
    private       byte[]  skyLight;
    private       byte[]  blockLight;

    /**
     *
     */
    public ChunkSection(
            int x, int y, int z, Palette blockPalette, Palette biomePalette, byte[] skyLight, byte[] blockLight
    ) {
        this.x            = x;
        this.y            = y;
        this.z            = z;
        this.blockPalette = blockPalette;
        this.biomePalette = biomePalette;
        this.skyLight     = skyLight;
        this.blockLight   = blockLight;
    }

    public ChunkSection(int x, int y, int z) {
        this(x, y, z, Palette.blocks(), Palette.biomes(), new byte[0], new byte[0]);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    public Palette blockPalette() {
        return blockPalette;
    }

    public Palette biomePalette() {
        return biomePalette;
    }

    public byte[] skyLight() {
        return skyLight;
    }

    public ChunkSection skyLight(byte[] skyLight) {
        this.skyLight = skyLight;
        return this;
    }

    public byte[] blockLight() {
        return blockLight;
    }

    public ChunkSection blockLight(byte[] blockLight) {
        this.blockLight = blockLight;
        return this;
    }

    public ChunkSection clear() {
        this.blockPalette.fill(0);
        this.biomePalette.fill(0);
        this.skyLight   = new byte[0];
        this.blockLight = new byte[0];
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (ChunkSection) obj;
        return this.x == that.x &&
               this.y == that.y &&
               this.z == that.z &&
               Objects.equals(this.blockPalette, that.blockPalette) &&
               Objects.equals(this.biomePalette, that.biomePalette);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, blockPalette, biomePalette);
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeShort((short) blockPalette.count());
        writer.write(blockPalette);
        writer.write(biomePalette);
    }
}
