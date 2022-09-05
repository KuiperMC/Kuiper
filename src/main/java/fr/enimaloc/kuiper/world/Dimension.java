/*
 * Dimension
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.world;

import fr.enimaloc.kuiper.objects.Identifier;
import java.util.Optional;

/**
 *
 */
public record Dimension(Identifier name,
                        boolean piglinSafe, boolean hasRaids, int monsterSpawnLightLevel,
                        int monsterSpawnBlockLightLimit,
                        boolean natural, float ambientLight, Optional<Float> fixedTime, Optional<Identifier> infiniburn,
                        boolean respawnAnchorWorks,
                        boolean skyLight,
                        boolean bedWorks, Identifier effects, int minY, int height, int logicalHeight,
                        double coordinateScale,
                        boolean ultrawarm, boolean ceiling) {

    public static final Dimension OVERWORLD = new Dimension(
            Identifier.minecraft("overworld"),
            false,
            true,
            7,
            0,
            true,
            0.0f,
            Optional.empty(),
            Optional.of(Identifier.minecraft("infiniburn_overworld")),
            false,
            true,
            true,
            Identifier.minecraft("overworld"),
            -64,
            384,
            384,
            1D,
            false,
            false);
    public static final Dimension THE_NETHER = new Dimension(
            Identifier.minecraft("the_nether"),
            true,
            false,
            11,
            15,
            false,
            0.1f,
            Optional.of(18000f),
            Optional.of(Identifier.minecraft("infiniburn_nether")),
            true,
            false,
            false,
            Identifier.minecraft("the_nether"),
            0,
            256,
            128,
            8.0d,
            true,
            true
    );

    public static Dimension[] values() {
        return new Dimension[]{OVERWORLD, THE_NETHER};
    }
}
