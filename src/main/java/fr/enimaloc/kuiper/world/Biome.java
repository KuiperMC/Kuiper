/*
 * Biome
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.world;

import fr.enimaloc.kuiper.objects.Identifier;
import java.util.Optional;
import java.util.OptionalInt;

/**
 *
 */
public record Biome(Identifier name,
                    Optional<String> precipitation, float temperature, float depth, float scale, float downfall,
                    Optional<String> category, Optional<String> temperatureModifier, int skyColor, int waterFogColor,
                    int fogColor, int waterColor, OptionalInt foliageColor, OptionalInt grassColor,
                    Optional<String> grassColorModifier, Optional<String> music, Optional<Sound> ambientSound,
                    Optional<Sound> additionsSound, Sound moodSound) {

    public static final Biome VOID   = new Biome(
            Identifier.minecraft("void"),
            Optional.empty(),
            -1,
            0.5f,
            -1,
            0.5f,
            Optional.empty(),
            Optional.empty(),
            8103167,
            329011,
            12638463,
            4159204,
            OptionalInt.empty(),
            OptionalInt.empty(),
            Optional.empty(),
            null,
            null,
            null,
            new Sound(Identifier.minecraft("ambient.cave"), 2.0f, 8, 6000)
    );
    public static final Biome PLAINS = new Biome(
            Identifier.minecraft("plains"),
            Optional.of("rain"),
            0.8f,
            1.0f,
            1f,
            0.4f,
            Optional.of("plains"),
            Optional.empty(),
            7907327,
            329011,
            12638463,
            4159204,
            OptionalInt.empty(),
            OptionalInt.empty(),
            Optional.empty(),
            null,
            null,
            null,
            new Sound(Identifier.minecraft("ambient.cave"), 2.0f, 8, 6000)
    );

    public static Biome[] values() {
        return new Biome[]{VOID, PLAINS};
    }

    public record Sound(Identifier name, float offset, int blockSearchExtent, long tickDelay) {
    }
}
