/*
 * Chunk
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.world;

/**
 *
 */

import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.network.data.Writeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.LongArrayTag;

public record Chunk(int x, int z, ChunkSection[] sections) implements Writeable {

    public ChunkSection sectionAt(int y) {
        return sections[y / 16];
    }

    public CompoundTag heightmaps() {
        CompoundTag heightmaps     = new CompoundTag();
        int         dimHeight      = 384;
        int[]       motionBlocking = new int[16 * 16];
        int[]       worldSurface   = new int[16 * 16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                motionBlocking[x + z * 16] = 0;
                worldSurface[x + z * 16]   = dimHeight - 1;
            }
        }
        int bitsForHeight = Integer.SIZE - Integer.numberOfLeadingZeros(dimHeight);
        heightmaps.put("MOTION_BLOCKING", new LongArrayTag(Utils.encodeBlocks(motionBlocking, bitsForHeight)));
        heightmaps.put("WORLD_SURFACE", new LongArrayTag(Utils.encodeBlocks(worldSurface, bitsForHeight)));
        return heightmaps;
    }

    public byte[] data() {
        try (BinaryWriter data = new BinaryWriter()) {
            for (ChunkSection section : sections) {
                data.write(section);
            }
            return data.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    @Override
    public void write(BinaryWriter writer) {
        writer.writeInt(x);
        writer.writeInt(z);
        writer.writeNBT(heightmaps());
        writer.writeByteArray(data(), SizedStrategy.VARINT);
//        writer.writeList(List.of(), null, SizedStrategy.VARINT);
        writer.writeVarInt(0);
        writer.write(lightData());
    }

    public LightData lightData() {
        BitSet       skyMask        = new BitSet();
        BitSet       blockMask      = new BitSet();
        BitSet       emptySkyMask   = new BitSet();
        BitSet       emptyBlockMask = new BitSet();
        List<byte[]> skyLights      = new ArrayList<>();
        List<byte[]> blockLights    = new ArrayList<>();

        int i = 0;
        for (ChunkSection section : sections) {
            i++;
            byte[] skyLight   = section.skyLight();
            byte[] blockLight = section.blockLight();
            if (skyLight.length != 0) {
                skyLights.add(skyLight);
                skyMask.set(i);
            } else {
                emptySkyMask.set(i);
            }
            if (blockLight.length != 0) {
                blockLights.add(blockLight);
                blockMask.set(i);
            } else {
                emptyBlockMask.set(i);
            }
        }
        return new LightData(true, skyMask, blockMask, emptySkyMask, emptyBlockMask, skyLights, blockLights);
    }

    class Utils {
        Utils() {
        }

        private static final int[] MAGIC = {
                -1, -1, 0, Integer.MIN_VALUE, 0, 0, 1431655765, 1431655765, 0, Integer.MIN_VALUE,
                0, 1, 858993459, 858993459, 0, 715827882, 715827882, 0, 613566756, 613566756,
                0, Integer.MIN_VALUE, 0, 2, 477218588, 477218588, 0, 429496729, 429496729, 0,
                390451572, 390451572, 0, 357913941, 357913941, 0, 330382099, 330382099, 0, 306783378,
                306783378, 0, 286331153, 286331153, 0, Integer.MIN_VALUE, 0, 3, 252645135, 252645135,
                0, 238609294, 238609294, 0, 226050910, 226050910, 0, 214748364, 214748364, 0,
                204522252, 204522252, 0, 195225786, 195225786, 0, 186737708, 186737708, 0, 178956970,
                178956970, 0, 171798691, 171798691, 0, 165191049, 165191049, 0, 159072862, 159072862,
                0, 153391689, 153391689, 0, 148102320, 148102320, 0, 143165576, 143165576, 0,
                138547332, 138547332, 0, Integer.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 126322567,
                126322567, 0, 122713351, 122713351, 0, 119304647, 119304647, 0, 116080197, 116080197,
                0, 113025455, 113025455, 0, 110127366, 110127366, 0, 107374182, 107374182, 0,
                104755299, 104755299, 0, 102261126, 102261126, 0, 99882960, 99882960, 0, 97612893,
                97612893, 0, 95443717, 95443717, 0, 93368854, 93368854, 0, 91382282, 91382282,
                0, 89478485, 89478485, 0, 87652393, 87652393, 0, 85899345, 85899345, 0,
                84215045, 84215045, 0, 82595524, 82595524, 0, 81037118, 81037118, 0, 79536431,
                79536431, 0, 78090314, 78090314, 0, 76695844, 76695844, 0, 75350303, 75350303,
                0, 74051160, 74051160, 0, 72796055, 72796055, 0, 71582788, 71582788, 0,
                70409299, 70409299, 0, 69273666, 69273666, 0, 68174084, 68174084, 0, Integer.MIN_VALUE,
                0, 5};

        public static long[] encodeBlocks(int[] blocks, int bitsPerEntry) {
            final long maxEntryValue = (1L << bitsPerEntry) - 1;
            final char valuesPerLong = (char) (64 / bitsPerEntry);
            final int  magicIndex    = 3 * (valuesPerLong - 1);
            final long divideMul     = Integer.toUnsignedLong(MAGIC[magicIndex]);
            final long divideAdd     = Integer.toUnsignedLong(MAGIC[magicIndex + 1]);
            final int  divideShift   = MAGIC[magicIndex + 2];
            final int  size          = (blocks.length + valuesPerLong - 1) / valuesPerLong;

            long[] data = new long[size];

            for (int i = 0; i < blocks.length; i++) {
                final long value     = blocks[i];
                final int  cellIndex = (int) (i * divideMul + divideAdd >> 32L >> divideShift);
                final int  bitIndex  = (i - cellIndex * valuesPerLong) * bitsPerEntry;
                data[cellIndex] = data[cellIndex] & ~(maxEntryValue << bitIndex) | (value & maxEntryValue) << bitIndex;
            }

            return data;
        }
    }

    public record LightData(boolean trustEdges, BitSet skyMask, BitSet blockMask, BitSet emptySkyMask,
                            BitSet emptyBlockMask,
                            List<byte[]> skyLights, List<byte[]> blockLights) implements Writeable {
        @Override
        public void write(BinaryWriter writer) {
            writer.writeBoolean(trustEdges);

            writer.writeLongArray(skyMask.toLongArray(), SizedStrategy.VARINT);
            writer.writeLongArray(blockMask.toLongArray(), SizedStrategy.VARINT);

            writer.writeLongArray(emptySkyMask.toLongArray(), SizedStrategy.VARINT);
            writer.writeLongArray(emptyBlockMask.toLongArray(), SizedStrategy.VARINT);

            writer.writeList(skyLights, (w, b) -> w.writeByteArray(b, SizedStrategy.VARINT), SizedStrategy.VARINT);
            writer.writeList(blockLights, (w, b) -> w.writeByteArray(b, SizedStrategy.VARINT), SizedStrategy.VARINT);
        }
    }
}
