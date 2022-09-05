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

    public static final Dimension WORLD = new Dimension(
            Identifier.kuiper("world"),
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

    public static Dimension[] values() {
        return new Dimension[]{WORLD};
    }
}
