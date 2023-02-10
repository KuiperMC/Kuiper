/*
 * PacketClassDescriptor
 *
 * 0.0.1
 *
 * 09/02/2023
 */
package fr.enimaloc.kuiper.logger;

import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 */
public class PacketClassDescriptor extends SimpleClassDescriptor {
    @Override
    protected Map<String, String> firstAdditionalFields() {
        try {
            Method getPacketId = getClass().getDeclaredMethod("getPacketId");
            return Map.of("packetId", getPacketId.trySetAccessible() ? getPacketId.invoke(this).toString() : "non-accessible");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return Map.of("packetId", e.toString());
        }
    }
}
