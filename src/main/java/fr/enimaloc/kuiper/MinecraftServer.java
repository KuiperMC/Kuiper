/*
 * MinecraftServer
 *
 * 0.0.1
 *
 * 02/09/2022
 */
package fr.enimaloc.kuiper;

import ch.qos.logback.classic.Logger;
import fr.enimaloc.kuiper.collections.Manager;
import fr.enimaloc.kuiper.data.ServerSettings;
import fr.enimaloc.kuiper.network.Connection;
import java.net.ServerSocket;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

/**
 *
 */
public class MinecraftServer {

    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MinecraftServer.class);

    private ServerSettings      settings          = new ServerSettings();
    private Manager<Connection> connectionManager = new Manager<>(Connection::new);

    public MinecraftServer() {
        LOGGER.info("Starting server on port {}", settings.port);
        try (ServerSocket server = new ServerSocket(settings.port)) {
            while (!server.isClosed()) {
                new Thread(connectionManager.create(server.accept())).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

        new MinecraftServer();
    }

    public static class Markers {
        public static final Marker NETWORK     = MarkerFactory.getMarker("NETWORK");
        public static final Marker NETWORK_IN  = MarkerFactory.getMarker("IN");
        public static final Marker NETWORK_OUT = MarkerFactory.getMarker("OUT");

        public static final Marker AUTH = MarkerFactory.getMarker("AUTH");
    }
}