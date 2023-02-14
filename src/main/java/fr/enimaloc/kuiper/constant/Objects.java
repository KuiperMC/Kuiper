/*
 * Objects
 *
 * 0.0.1
 *
 * 11/02/2023
 */
package fr.enimaloc.kuiper.constant;

import com.google.gson.*;
import fr.enimaloc.kuiper.network.packet.status.ClientboundStatusResponse;

import java.lang.reflect.Type;

/**
 *
 */
public class Objects {
    private Objects() {}

    public static final Gson GSON = new Gson().newBuilder()
            .registerTypeHierarchyAdapter(ClientboundStatusResponse.Favicon.class, new FaviconTypeAdapter()).create();

    private static class FaviconTypeAdapter implements JsonDeserializer<ClientboundStatusResponse.Favicon>, JsonSerializer<ClientboundStatusResponse.Favicon> {
        @Override
        public ClientboundStatusResponse.Favicon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new ClientboundStatusResponse.Favicon(json.getAsString());
        }

        @Override
        public JsonElement serialize(ClientboundStatusResponse.Favicon src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getBase64());
        }
    }
}
