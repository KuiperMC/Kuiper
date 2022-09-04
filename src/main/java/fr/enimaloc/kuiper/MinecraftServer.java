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
import fr.enimaloc.kuiper.extra.MojangAuth;
import fr.enimaloc.kuiper.network.Connection;
import fr.enimaloc.kuiper.utils.StringUtils;
import java.net.ServerSocket;
import java.util.Arrays;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

/**
 *
 */
public class MinecraftServer {

    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MinecraftServer.class);

    public ServerSettings      settings          = new ServerSettings();
    private Manager<Connection> connectionManager = new Manager<>(Connection::new);

    public MinecraftServer() {
        LOGGER.info("Starting server on port {}", settings.port);
        if (settings.onlineMode) {
            MojangAuth.init();
            LOGGER.info("MojangAuth initialized");
        } else {
            if (LOGGER.isWarnEnabled()) {
                Arrays.stream(StringUtils.genFrame("""
                                               WARNING: Server is running in offline mode!
                                               WARNING: This is not recommended as it will allow hackers to connect with any username they choose.
                                               WARNING: To change this, set "online-mode" to "true" in the server.properties file.
                                               """, '+', '-', '|').split("\n"))
                      .forEach(LOGGER::warn);
            }
        }
        try (ServerSocket server = new ServerSocket(settings.port)) {
            while (!server.isClosed()) {
                new Thread(connectionManager.create(server.accept(), this)).start();
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
