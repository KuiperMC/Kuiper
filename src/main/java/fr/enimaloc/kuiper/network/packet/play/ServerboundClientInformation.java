/*
 * ServerboundClientInformation
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.chat.ChatMode;
import fr.enimaloc.kuiper.entities.Player;
import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.objects.Gamemode;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import fr.enimaloc.kuiper.world.Chunk;
import fr.enimaloc.kuiper.world.ChunkSection;
import java.util.Locale;
import org.slf4j.event.Level;

import static fr.enimaloc.kuiper.MinecraftServer.Markers.*;

/**
 *
 */
public class ServerboundClientInformation extends SimpleClassDescriptor implements Packet.Serverbound {

    public final Locale  locale;
    public final byte     viewDistance;
    public final ChatMode chatMode;
    public final boolean  chatColors;
    public final byte    displayedSkinParts;
    public final int    mainHand;
    public final boolean textFiltering;
    public final boolean serverListing;

    public ServerboundClientInformation(BinaryReader reader) {
        this.locale = Locale.forLanguageTag(reader.readString(SizedStrategy.VARINT).replace('_', '-'));
        this.viewDistance = reader.readByte();
        this.chatMode = ChatMode.values()[reader.readVarInt()];
        this.chatColors = reader.readBoolean();
        this.displayedSkinParts = reader.readByte();
        this.mainHand = reader.readVarInt();
        this.textFiltering = reader.readBoolean();
        this.serverListing = reader.readBoolean();
    }

    @Override
    public int id() {
        return 0x07;
    }

    @Override
    public void handle(Connection connection) {
        Player player = connection.player;
        player.locale = locale;
        player.viewDistance = viewDistance;
        player.chatMode = chatMode;
        player.chatColors = chatColors;
        player.displayedSkinParts = displayedSkinParts;
        player.rightMainHand      = mainHand == 1;
        player.textFiltering      = textFiltering;
        player.serverListing      = serverListing;
        connection.sendPacket(new ClientboundSetCarriedItem().slot((byte) 0));
        connection.sendPacket(
                new ClientboundUpdateRecipe());
//                        .recipes(new ClientboundUpdateRecipe.Recipe
//                                .CraftingShapeless(Identifier.kuiper("test"))
//                                         .group(Identifier.kuiper("group_test"))
//                                         .addIngredient(
//                                                 new ClientboundUpdateRecipe.Recipe.Ingredient(
//                                                         new Slot().id(1)
//                                                                   .count((byte) 1)))
//                                         .result(new Slot().id(2).count((byte) 1))));
        connection.sendPacket(new ClientboundUpdateTags()
                                      .block(new ClientboundUpdateTags.Tag("wool", 35))
                                      .entityType(new ClientboundUpdateTags.Tag("frog_food", 55, 62))
                                      .fluid(new ClientboundUpdateTags.Tag("water", 8, 9))
                                      .fluid(new ClientboundUpdateTags.Tag("lava", 10, 11))
                                      .gameEvent(new ClientboundUpdateTags.Tag("mob_death",
                                                                               3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                                                                               13, 14, 15, 16, 17, 18, 19, 20, 21,
                                                                               22, 23, 24, 25, 26, 27, 28, 29, 30,
                                                                               31, 32, 33, 34, 35, 36, 37, 38, 39,
                                                                               40, 41, 42, 43, 44, 45, 46, 47, 48,
                                                                               49, 50, 51, 52, 53, 54, 55, 56, 57,
                                                                               58, 59, 60, 61, 62, 63, 64, 65, 66,
                                                                               67, 68, 69, 70, 71, 72, 73, 74, 75,
                                                                               76, 77, 78, 79, 80, 81, 82, 83, 84,
                                                                               85, 86, 87, 88, 89, 90, 91, 92, 93,
                                                                               94, 95, 96, 97, 98, 99, 100, 101, 102,
                                                                               103, 104, 105, 106, 107, 108, 109,
                                                                               110, 111, 112, 113, 114, 115, 116, 117,
                                                                               118, 119, 120, 121, 122, 123, 124, 125,
                                                                               126, 127, 128, 129, 130, 131, 132, 133,
                                                                               134, 135, 136, 137, 138, 139, 140, 141,
                                                                               142, 143, 144, 145, 146, 147, 148, 149,
                                                                               150, 151, 152, 153, 154, 155, 156, 157))
                                      .item(new ClientboundUpdateTags.Tag("coals", 263, 173)));
        connection.sendPacket(new ClientboundEntityEvent().entityId(0).status(Player.Status.OP_LEVEL_4));
        connection.sendPacket(new ClientboundCommandsPacket());
        connection.sendPacket(new ClientboundPlayerInfo().add(connection.player.uuid(),
                                                              connection.player.name(),
                                                              Gamemode.CREATIVE,
                                                              0,
                                                              null));
        connection.sendPacket(new ClientboundPlayerInfo().updateLatency(connection.player.uuid(), 100));
        connection.sendPacket(new ClientboundSetCenterChunk().x(0).z(0));
        int i1 = 0, i2 = 0;
        for (int x = -connection.server.settings.viewDistance; x <= connection.server.settings.viewDistance; x++) {
            i1++;
            i2 = 0;
            for (int z = -connection.server.settings.viewDistance; z <= connection.server.settings.viewDistance; z++) {
                i2++;
                ChunkSection section = new ChunkSection(x, 0, z);
                section.blockPalette().fill(1);
                Chunk        chunk        = new Chunk(x, z, new ChunkSection[]{section});
                connection.sendPacket(new ClientboundLightUpdate().chunk(chunk));
            }
        }
        Connection.LOGGER.makeLoggingEventBuilder(Level.INFO)
                         .addMarker(NETWORK)
                         .addMarker(NETWORK_OUT)
                         .log("Sent {}x{} chunks for a total of {} chunks", i1, i2, i1 * i2);
    }
}
