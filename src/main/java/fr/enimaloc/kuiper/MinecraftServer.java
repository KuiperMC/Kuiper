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
import fr.enimaloc.kuiper.mojang.auth.MojangAuth;
import fr.enimaloc.kuiper.network.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

/**
 *
 */
public class MinecraftServer extends Thread {

    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MinecraftServer.class);

    private final ServerSettings      settings;
    private final Manager<Connection> connectionManager = new Manager<>(Connection::new);
    private final ServerSocket        server;
    private       boolean             running = false;

    public MinecraftServer() throws IOException {
        this(new ServerSettings());
    }

    public MinecraftServer(ServerSettings settings) throws IOException {
        this.settings = settings;
        this.server = new ServerSocket(settings.port);
        this.start();
    }

    @Override
    public void run() {
        LOGGER.info("Starting server on port {}", settings.port);
        MojangAuth.init();
        running = true;
        while (!server.isClosed()) {
            try {
                new Thread(connectionManager.create(server.accept(), this)).start();
            } catch (IOException e) {
                if (running) { // Ignore if shutdown
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void shutdown() throws IOException {
        LOGGER.info("Shutting down server");
        running = false;
        this.interrupt();
        server.close();
        try {
            this.join();
        } catch (InterruptedException e) {
            LOGGER.trace("Interrupted while waiting for server to stop, not really a problem, but logging it anyway", e);
        }
        LOGGER.info("Server stopped");
        LOGGER.info("Thanks for using Kuiper !");
    }

    public ServerSettings getSettings() {
        return settings;
    }

    public Manager<Connection> getConnectionManager() {
        return connectionManager;
    }

    public static void main(String[] args) throws IOException {
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
