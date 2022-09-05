/*
 * Connection
 *
 * 0.0.1
 *
 * 02/09/2022
 */
package fr.enimaloc.kuiper.network;

import ch.qos.logback.classic.Logger;
import fr.enimaloc.kuiper.GameState;
import fr.enimaloc.kuiper.MinecraftServer;
import fr.enimaloc.kuiper.entities.Player;
import fr.enimaloc.kuiper.network.data.BinaryReader;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.packet.play.ClientboundChangeDifficulty;
import fr.enimaloc.kuiper.network.packet.play.ClientboundLogin;
import fr.enimaloc.kuiper.network.packet.play.ClientboundCustomPayload;
import fr.enimaloc.kuiper.network.packet.play.ClientboundPlayerAbilities;
import fr.enimaloc.kuiper.network.packet.unknown.ServerboundHandshake;
import fr.enimaloc.kuiper.objects.Gamemode;
import fr.enimaloc.kuiper.objects.Identifier;
import fr.enimaloc.kuiper.utils.VarIntUtils;
import fr.enimaloc.kuiper.world.Biome;
import fr.enimaloc.kuiper.world.Difficulty;
import fr.enimaloc.kuiper.world.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.querz.nbt.io.SNBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import javax.crypto.SecretKey;

import static fr.enimaloc.kuiper.MinecraftServer.Markers.*;

/**
 *
 */
public class Connection implements Runnable {

    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(Connection.class);

    @NotNull public final  MinecraftServer server;
    @NotNull private final Socket          socket;
    @NotNull private final List<Packet>    exchangedPackets = new ArrayList<>();
    public                 GameState       gameState        = GameState.UNKNOWN;
    @Nullable public       Player          player;
    @Nullable
    public                 SecretKey       encryptionKey;
    @Nullable private      Packet          lastReceived;
    @Nullable public       byte[]          nonce;

    public Connection(@NotNull Socket socket, @NotNull MinecraftServer server) {
        LOGGER.debug(NETWORK, "New connection from {}", socket.getInetAddress().getHostAddress());
        this.socket = socket;
        this.server = server;
    }

    public Connection(Object... args) {
        this((Socket) args[0], (MinecraftServer) args[1]);
    }

    @Override
    public void run() {
        InputStream inputStream;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (!socket.isClosed()) {
            try {
                VarIntUtils.VarInt lenVarInt = VarIntUtils.readVarInt(inputStream);
                if (lenVarInt == null) {
                    lastReceived = null;
                    continue;
                }
                try (BinaryReader reader = new BinaryReader(ByteBuffer.allocate(lenVarInt.value() + lenVarInt.length())
                                                                      .put(VarIntUtils.getVarInt(lenVarInt.value()))
                                                                      .put(inputStream.readNBytes(lenVarInt.value()))
                                                                      .rewind())) {

                    int                length   = reader.readVarInt();
                    int                packetId = reader.readVarInt();
                    Packet.Serverbound packet   = Packet.Serverbound.get(packetId, gameState).build(reader);
                    LOGGER.makeLoggingEventBuilder(Level.DEBUG)
                          .addMarker(NETWORK)
                          .addMarker(NETWORK_IN)
                          .log("{} -> SERVER: {}", socket.getInetAddress().getHostAddress(), packet);

                    if (LOGGER.isTraceEnabled()) {
                        AtomicInteger i = new AtomicInteger(0);
                        LOGGER.makeLoggingEventBuilder(Level.TRACE)
                              .addMarker(NETWORK)
                              .addMarker(NETWORK_IN)
                              .log("{} => SERVER: {}",
                                   socket.getInetAddress().getHostAddress(),
                                   Stream.generate(() -> reader.getBuffer().array()[i.getAndIncrement()])
                                         .limit(reader.getBuffer().limit())
                                         .map(j -> String.format("%02x", j))
                                         .collect(Collectors.joining(" ")));
                    }

//            int length = lenVarInt.value();
//            return ByteBuffer.allocate(length + lenVarInt.length())
//                             .put(VarIntUtils.getVarInt(length))
//                             .put(inputStream.readNBytes(length));

                    if (packet instanceof ServerboundHandshake) {
                        packet.handle(this);
                    } else {
                        new Thread(() -> packet.handle(this)).start();
                    }
                    exchangedPackets.add(packet);
                    lastReceived = packet;
                }
            } catch (IOException e) {
//                LOGGER.makeLoggingEventBuilder(Level.ERROR)
//                      .addMarker(NETWORK)
//                      .setCause(e)
//                      .log("Error while reading packet");
                terminate(e);
            }
        }
    }

    public void sendPacket(Packet.Clientbound packet) {
        exchangedPackets.add(packet);
        LOGGER.makeLoggingEventBuilder(Level.DEBUG)
              .addMarker(NETWORK)
              .addMarker(NETWORK_OUT)
              .log("SERVER -> {}: {}",
                   socket.getInetAddress().getHostAddress(),
                   packet);

        try (BinaryWriter writer = new BinaryWriter(
                BinaryWriter.makeArray(packet::write).length
                + VarIntUtils.varIntSize(packet.id()), false).writeVarInt(packet.id())
                                                             .write(packet)) {
            byte[] varInt = VarIntUtils.getVarInt(writer.getBuffer().array().length);
            byte[] bytes  = new byte[writer.getBuffer().array().length + varInt.length];
            System.arraycopy(varInt, 0, bytes, 0, varInt.length);
            System.arraycopy(writer.getBuffer().array(), 0, bytes, varInt.length, writer.getBuffer().array().length);

            if (LOGGER.isTraceEnabled()) {
                AtomicInteger i = new AtomicInteger(0);
                LOGGER.makeLoggingEventBuilder(Level.TRACE)
                      .addMarker(NETWORK)
                      .addMarker(NETWORK_OUT)
                      .log("SERVER => {}: {}",
                           socket.getInetAddress().getHostAddress(),
                           Stream.generate(() -> bytes[i.getAndIncrement()])
                                 .limit(bytes.length)
                                 .map(j -> String.format("%02x", j))
                                 .collect(Collectors.joining(" ")));
            }

            socket.getOutputStream().write(bytes);
            socket.getOutputStream().flush();
        } catch (IOException e) {
            terminate(e);
        }
    }

    public void terminate(Throwable e) {
        LOGGER.error(NETWORK, "Connection terminated", e);
        terminate();
    }

    public void terminate(String reason) {
        LOGGER.debug(NETWORK, "Connection terminated: {}", reason);
        terminate();
    }

    public void terminate() {
        try {
            socket.close();
        } catch (IOException e) {
            LOGGER.error(NETWORK, "Error while closing socket", e);
        }
    }

    public InetAddress getRemoteAddress() {
        return socket.getInetAddress();
    }

    public void beginPlayState() {
        this.gameState = GameState.PLAY;
        this.sendPacket(new ClientboundLogin().entityId(0)
                                .gamemode(Gamemode.CREATIVE)
                                .previousGamemode(Gamemode.CREATIVE)
                                .dimensions(Identifier.minecraft("overworld"))
                                .registry(getRegistryCodec())
                                .dimensionType(Identifier.minecraft("overworld"))
                                .dimensionName(Identifier.minecraft("world"))
                                .viewDistance(2)
                                .simulationDistance(2)
                                .debug(true));
        this.sendPacket(ClientboundCustomPayload.brand().data("Kuiper"));
        this.sendPacket(new ClientboundChangeDifficulty().difficulty(Difficulty.PEACEFUL).locked(false));
        this.sendPacket(new ClientboundPlayerAbilities().flying(true));
    }

    private CompoundTag getRegistryCodec() {

        CompoundTag registryCodec = new CompoundTag();

        AtomicInteger id                = new AtomicInteger(0);
        CompoundTag   dimensionRegistry = new CompoundTag();
        dimensionRegistry.putString("type", "minecraft:dimension_type");

        ListTag<CompoundTag> dimensionList = new ListTag<>(CompoundTag.class);
        for (Dimension spec : Dimension.values()) {
            if (spec == null) {
                continue;
            }
            CompoundTag identifierTag = new CompoundTag();
            identifierTag.putString("name", spec.name().toString());
            identifierTag.putInt("id", id.getAndIncrement());

            CompoundTag specTag = new CompoundTag();
            specTag.putBoolean("piglin_safe", spec.piglinSafe());
            specTag.putBoolean("has_raids", spec.hasRaids());
            {
                CompoundTag monsterSpawnLightLevel = new CompoundTag();
                monsterSpawnLightLevel.putString("type", "minecraft:uniform");
                CompoundTag monsterSpawnLightLevelValue = new CompoundTag();
                monsterSpawnLightLevelValue.putDouble("min_inclusive", 0);
                monsterSpawnLightLevelValue.putDouble("max_inclusive", spec.monsterSpawnLightLevel());
                monsterSpawnLightLevel.put("value", monsterSpawnLightLevelValue);
                specTag.put("monster_spawn_light_level", monsterSpawnLightLevel);
            }
            specTag.putInt("monster_spawn_block_light_limit", spec.monsterSpawnBlockLightLimit());
            specTag.putBoolean("natural", spec.natural());
            specTag.putFloat("ambient_light", spec.ambientLight());
            spec.fixedTime().ifPresent(fixedTime -> specTag.putFloat("fixed_time", fixedTime));
            specTag.putString("infiniburn", "#" + spec.infiniburn().map(Identifier::toString).orElse(""));
            specTag.putBoolean("respawn_anchor_works", spec.respawnAnchorWorks());
            specTag.putBoolean("has_skylight", spec.skyLight());
            specTag.putBoolean("bed_works", spec.bedWorks());
            specTag.putString("effects", spec.effects().toString());
            specTag.putInt("min_y", spec.minY());
            specTag.putInt("height", spec.height());
            specTag.putInt("logical_height", spec.logicalHeight());
            specTag.putDouble("coordinate_scale", spec.coordinateScale());
            specTag.putBoolean("ultrawarm", spec.ultrawarm());
            specTag.putBoolean("has_ceiling", spec.ceiling());

            identifierTag.put("element", specTag);

            dimensionList.add(identifierTag);
        }
        dimensionRegistry.put("value", dimensionList);
        registryCodec.put("minecraft:dimension_type", dimensionRegistry);

        id.set(0);
        CompoundTag          biomeRegistry = new CompoundTag();
        ListTag<CompoundTag> biomeList     = new ListTag<>(CompoundTag.class);
        biomeRegistry.putString("type", "minecraft:worldgen/biome");

        for (Biome spec : Biome.values()) {
            if (spec == null) {
                continue;
            }
            CompoundTag identifierTag = new CompoundTag();
            identifierTag.putString("name", spec.name().toString());
            identifierTag.putInt("id", id.getAndIncrement());

            CompoundTag specTag = new CompoundTag();
            specTag.putString("precipitation", spec.precipitation().orElse("none"));
            specTag.putFloat("depth", spec.depth());
            specTag.putFloat("temperature", spec.temperature());
            specTag.putFloat("scale", spec.scale());
            specTag.putFloat("downfall", spec.downfall());
            spec.category().ifPresent(category -> specTag.putString("category", category));
            spec.temperatureModifier().ifPresent(
                    temperatureModifier -> specTag.putString("temperature_modifier", temperatureModifier));
            {
                CompoundTag effectTag = new CompoundTag();
                effectTag.putInt("sky_color", spec.skyColor());
                effectTag.putInt("water_fog_color", spec.waterFogColor());
                effectTag.putInt("water_color", spec.waterColor());
                effectTag.putInt("fog_color", spec.fogColor());
                spec.foliageColor().ifPresent(foliageColor -> effectTag.putInt("foliage_color", foliageColor));
                spec.grassColor().ifPresent(grassColor -> effectTag.putInt("grass_color", grassColor));
                spec.grassColorModifier().ifPresent(
                        grassColorModifier -> effectTag.putString("grass_color_modifier", grassColorModifier));
                {
                    CompoundTag moodSoundTag = new CompoundTag();
                    moodSoundTag.putString("sound", spec.moodSound().name().toString());
                    moodSoundTag.putFloat("offset", spec.moodSound().offset());
                    moodSoundTag.putInt("block_search_extent", spec.moodSound().blockSearchExtent());
                    moodSoundTag.putLong("tick_delay", spec.moodSound().tickDelay());
                    effectTag.put("mood_sound", moodSoundTag);
                }
                specTag.put("effects", effectTag);
            }

            identifierTag.put("element", specTag);
            biomeList.add(identifierTag);
        }
        biomeRegistry.put("value", biomeList);
        registryCodec.put("minecraft:worldgen/biome", biomeRegistry);
        try {
            registryCodec.put("minecraft:chat_type", SNBTUtil.fromSNBT("""
                                                                                   {
                                                                                       "type": "minecraft:chat_type",
                                                                                       "value": [
                                                                                            {
                                                                                               "name":"minecraft:chat",
                                                                                               "id":1,
                                                                                               "element":{
                                                                                                  "chat":{
                                                                                                     "translation_key":"chat.type.text",
                                                                                                     "parameters":[
                                                                                                        "sender",
                                                                                                        "content"
                                                                                                     ]
                                                                                                  },
                                                                                                  "narration":{
                                                                                                     "translation_key":"chat.type.text.narrate",
                                                                                                     "parameters":[
                                                                                                        "sender",
                                                                                                        "content"
                                                                                                     ]
                                                                                                  }
                                                                                               }
                                                                                            }
                                                                                       ]
                                                                                   }"""));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        CompoundTag chatTag = new CompoundTag();
        CompoundTag system = new CompoundTag();/*.writeCompound("chat", new Tag())
                                  .writeCompound("narration", new Tag().writeString("priority", "system"));*/
        system.put("chat", new CompoundTag());
        CompoundTag narration = new CompoundTag();
        narration.putString("priority", "system");
        system.put("narration", narration);
        CompoundTag gameInfo = new CompoundTag();//.writeCompound("overlay", new Tag());
        gameInfo.put("overlay", new CompoundTag());
        chatTag.putString("type", "minecraft:chat_type");
//            chatTag.
//                   .writeList("value", Tag.Type.COMPOUND, Arrays.asList(
//                           new Tag().writeCompound("element", system)
//                                   .writeString("name", "minecraft:system")
//                                   .writeInt("id", 0),
//                           new Tag().writeCompound("element",  gameInfo)
//                                   .writeString("name", "minecraft:game_info")
//                                   .writeInt("id", 1)));
        ListTag<CompoundTag> chatList = new ListTag<>(CompoundTag.class);
        CompoundTag          system1  = new CompoundTag();
        system1.put("element", system);
        system1.putString("name", "minecraft:system");
        system1.putInt("id", 0);
        chatList.add(system1);
        CompoundTag gameInfo1 = new CompoundTag();
        gameInfo1.put("element", gameInfo);
        gameInfo1.putString("name", "minecraft:game_info");
        gameInfo1.putInt("id", 1);
        chatList.add(gameInfo1);
        chatTag.put("value", chatList);
        registryCodec.put("minecraft:chat_type", chatTag);

        return registryCodec;
    }
}
