/*
 * ClientboundUpdateTags
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.network.data.Writeable;
import fr.enimaloc.kuiper.objects.Identifier;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import java.util.*;

/**
 *
 */
public class ClientboundUpdateTags extends SimpleClassDescriptor implements Packet.Clientbound {

    private Map<Identifier, List<Tag>> tags = new HashMap<>();

    @Override
    public int id() {
        return 0x68;
    }

    public ClientboundUpdateTags tags(
            Map<Identifier, List<Tag>> tags
    ) {
        this.tags = tags;
        return this;
    }

    public ClientboundUpdateTags tags(Identifier identifier, List<Tag> tags) {
        if (this.tags == null) {
            this.tags = new HashMap<>();
        }
        if (this.tags.containsKey(identifier)) {
            this.tags.get(identifier).addAll(tags);
        } else {
            if (tags.getClass().getName().contains("Unmodifiable") || tags.getClass().getName().contains("Immutable")) {
                this.tags.put(identifier, new ArrayList<>(tags));
            } else {
                this.tags.put(identifier, tags);
            }
        }
        return this;
    }

    public ClientboundUpdateTags tags(String identifier, List<Tag> tag) {
        return tags(Identifier.minecraft(identifier), tag);
    }

    public ClientboundUpdateTags tags(Identifier identifier, Tag tag) {
        return tags(identifier, List.of(tag));
    }

    public ClientboundUpdateTags tags(String identifier, Tag tag) {
        return tags(Identifier.minecraft(identifier), tag);
    }

    public ClientboundUpdateTags block(List<Tag> tags) {
        return tags(Identifier.minecraft("block"), tags);
    }

    public ClientboundUpdateTags block(Tag tag) {
        return tags(Identifier.minecraft("block"), tag);
    }

    public ClientboundUpdateTags item(List<Tag> tags) {
        return tags(Identifier.minecraft("item"), tags);
    }

    public ClientboundUpdateTags item(Tag tag) {
        return tags(Identifier.minecraft("item"), tag);
    }

    public ClientboundUpdateTags fluid(List<Tag> tags) {
        return tags(Identifier.minecraft("fluid"), tags);
    }

    public ClientboundUpdateTags fluid(Tag tag) {
        return tags(Identifier.minecraft("fluid"), tag);
    }

    public ClientboundUpdateTags entityType(List<Tag> tags) {
        return tags(Identifier.minecraft("entity_type"), tags);
    }

    public ClientboundUpdateTags entityType(Tag tag) {
        return tags(Identifier.minecraft("entity_type"), tag);
    }

    public ClientboundUpdateTags gameEvent(List<Tag> tags) {
        return tags(Identifier.minecraft("game_event"), tags);
    }

    public ClientboundUpdateTags gameEvent(Tag tag) {
        return tags(Identifier.minecraft("game_event"), tag);
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeList(tags.entrySet(), (w, entry) -> {
            w.write(entry.getKey());
            w.writeList(entry.getValue(), BinaryWriter::write, SizedStrategy.VARINT);
        }, SizedStrategy.VARINT);
    }

    public record Tag(Identifier identifier, List<Integer> ids) implements Writeable {
        public Tag(String identifier, List<Integer> ids) {
            this(Identifier.minecraft(identifier), ids);
        }

        public Tag(Identifier identifier, Integer... ids) {
            this(identifier, List.of(ids));
        }

        public Tag(String identifier, Integer... ids) {
            this(identifier, List.of(ids));
        }

        @Override
        public void write(BinaryWriter writer) {
            writer.write(identifier);
            writer.writeList(ids, BinaryWriter::writeVarInt, SizedStrategy.VARINT);
        }
    }
}
