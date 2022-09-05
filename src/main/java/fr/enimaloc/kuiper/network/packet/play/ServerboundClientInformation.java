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
import fr.enimaloc.kuiper.objects.Identifier;
import fr.enimaloc.kuiper.objects.Slot;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import java.util.Locale;

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
        player.rightMainHand = mainHand == 1;
        player.textFiltering = textFiltering;
        player.serverListing = serverListing;
        connection.sendPacket(new ClientboundSetCarriedItem().slot((byte) 4));
        connection.sendPacket(
                new ClientboundUpdateRecipe()
                        .recipes(new ClientboundUpdateRecipe.Recipe
                                .CraftingShapeless(Identifier.kuiper("test"))
                                         .group(Identifier.kuiper("group_test"))
                                         .addIngredient(
                                                 new ClientboundUpdateRecipe.Recipe.Ingredient(
                                                         new Slot().id(1)
                                                                   .count((byte) 1)))
                                         .result(new Slot().id(2).count((byte) 1))));
    }
}
