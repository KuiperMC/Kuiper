/*
 * MinecraftServer
 *
 * 0.0.1
 *
 * 02/09/2022
 */
package fr.enimaloc.kuiper;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

/**
 *
 */
public class MinecraftServer {

    public static void main(String[] args) {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

        System.out.println("MinecraftServer.main");
    }

    public static class Markers {
        public static final Marker NETWORK = MarkerFactory.getMarker("NETWORK");
        public static final Marker NETWORK_IN = MarkerFactory.getMarker("IN");
        public static final Marker NETWORK_OUT = MarkerFactory.getMarker("OUT");

        public static final Marker AUTH     = MarkerFactory.getMarker("AUTH");
    }
}
