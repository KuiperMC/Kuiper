/*
 * Logger
 *
 * 0.0.1
 *
 * 01/09/2022
 */
package fr.enimaloc.kuiper.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.CachingDateFormatter;

import java.nio.charset.StandardCharsets;

/**
 *
 */
public class LoggerConfigurator extends ContextAwareBase implements Configurator {
    public static final boolean SHOW_LOGGER_CONFIG    = "true".equals(System.getenv("LOGGER_SHOW_CONFIG"));
    public static final boolean REDUCED               = "true".equals(System.getenv("LOGGER_REDUCED"));
    public static final boolean DISPLAY_IF_NOT_SOURCE = "true".equals(System.getenv("LOGGER_DISPLAY_IF_NOT_SOURCE"));
    public static final boolean DISPLAY_SOURCE        = "true".equals(System.getenv("LOGGER_DISPLAY_SOURCE"));
    public static final int     MAX_STACKTRACE_DEPTH  = Integer.parseInt(System.getenv().getOrDefault("LOGGER_MAX_STACK_TRACE_DEPTH", "10"));
    public static final String  DATE_FORMAT           = System.getenv().getOrDefault("LOGGER_DATE_FORMAT", CoreConstants.ISO8601_PATTERN);
    public static final String  LOG_LEVEL             = System.getenv().getOrDefault("LOGGER_LEVEL", Level.TRACE.levelStr);

    public static final String ERROR_COLOR = "\u001B[31m";
    public static final String WARN_COLOR  = "\u001B[33m";
    public static final String INFO_COLOR  = "\u001B[32m";
    public static final String DEBUG_COLOR = "\u001B[36m";
    public static final String TRACE_COLOR = "\u001B[35m";
    public static final String RESET_COLOR = "\u001B[0m";

    @Override
    public ExecutionStatus configure(LoggerContext loggerContext) {
        Layout layout = new Layout();
        layout.setContext(loggerContext);
        layout.start();

        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(loggerContext);
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.setLayout(layout);

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(loggerContext);
        appender.setName("console");
        appender.setEncoder(encoder);
        appender.start();

        Logger root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setAdditive(false);
        root.addAppender(appender);

        if (root.isTraceEnabled() || SHOW_LOGGER_CONFIG) {
            root.setLevel(Level.TRACE);
            root.trace("Logger configuration:");
            root.trace("  - Reduced: {}", REDUCED);
            root.trace("  - Display if not source: {}", DISPLAY_IF_NOT_SOURCE);
            root.trace("  - Display source: {}", DISPLAY_SOURCE);
            root.trace("  - Max stack trace depth: {}", MAX_STACKTRACE_DEPTH);
            root.trace("  - Date format: {}", DATE_FORMAT);
            root.trace("  - Root log level: {}", LOG_LEVEL);
        }
        root.setLevel(Level.toLevel(LOG_LEVEL));

        return ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY;
    }

    static class Layout extends LayoutBase<ILoggingEvent> {
        @Override
        public String doLayout(ILoggingEvent event) {
            StringBuilder builder = new StringBuilder()
                    .append(getLevelColor(event.getLevel()))
                    .append("[")
                    .append(new CachingDateFormatter(DATE_FORMAT).format(event.getTimeStamp()))
                    .append("] ");
            if (!REDUCED) {
                builder.append("[")
                        .append(event.getLevel())
                        .append("] ");
                if (event.getMarkerList() != null) {
                    builder.append(event.getMarkerList()).append(" ");
                }
                builder.append("[")
                        .append(event.getThreadName())
                        .append("] ");
            }
            builder.append("[")
                    .append(event.getLoggerName().substring(event.getLoggerName().lastIndexOf('.') + 1));

            if (DISPLAY_SOURCE && !REDUCED) {
                StackTraceElement datum     = event.getCallerData()[0];
                int               i         = 1;
                boolean           isLogging = datum.getMethodName().startsWith("log");
                //noinspection SpellCheckingInspection
                while (datum.getClassName().startsWith("uk.org.lidalia.sysoutslf4j") && isLogging) {
                    datum = event.getCallerData()[i++];
                }

                if (datum.isNativeMethod() && DISPLAY_IF_NOT_SOURCE) {
                    builder.append(" - Native Method] ");
                } else if (DISPLAY_IF_NOT_SOURCE || datum.getClassName().startsWith("fr.enimaloc")) {
                    builder.append(" - ").append(
                                    datum.getClassName().substring(datum.getClassName().lastIndexOf('.') + 1))
                            .append(".")
                            .append(datum.getMethodName())
                            .append("(")
                            .append(datum.getFileName())
                            .append(":")
                            .append(datum.getLineNumber())
                            .append(")] ");
                } else {
                    builder.append("] ");
                }
            } else {
                builder.append("] ");
            }

            builder.append(event.getFormattedMessage())
                    .append(CoreConstants.LINE_SEPARATOR);
            if (event.getThrowableProxy() != null) {
                ThrowableProxy throwableProxy = (ThrowableProxy) event.getThrowableProxy();
                builder.append(throwableProxy.getThrowable().toString())
                        .append(CoreConstants.LINE_SEPARATOR);
                for (int j = 0; j < throwableProxy.getStackTraceElementProxyArray().length; j++) {
                    if (j >= MAX_STACKTRACE_DEPTH) {
                        builder.append("\t... ")
                                .append(throwableProxy.getStackTraceElementProxyArray().length - MAX_STACKTRACE_DEPTH)
                                .append(" more")
                                .append(CoreConstants.LINE_SEPARATOR);
                        break;
                    }
                    builder.append("\tat ")
                            .append(throwableProxy.getStackTraceElementProxyArray()[j].getStackTraceElement())
                            .append(CoreConstants.LINE_SEPARATOR);
                }
            }

            return builder.append(RESET_COLOR).toString();
        }

        String getLevelColor(Level level) {
            return switch (level.levelInt) {
                case Level.ERROR_INT -> ERROR_COLOR;
                case Level.WARN_INT -> WARN_COLOR;
                case Level.INFO_INT -> INFO_COLOR;
                case Level.DEBUG_INT -> DEBUG_COLOR;
                case Level.TRACE_INT -> TRACE_COLOR;
                default -> RESET_COLOR;
            };
        }
    }
}
