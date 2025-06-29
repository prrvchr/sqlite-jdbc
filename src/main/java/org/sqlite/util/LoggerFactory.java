package org.sqlite.util;

import java.util.function.Supplier;
import java.lang.System.Logger.Level;

/**
 * A factory for {@link Logger} instances that uses SLF4J if present, falling back on a
 * java.util.logging implementation otherwise.
 */
public class LoggerFactory {

    /**
     * Get a {@link Logger} instance for the given host class.
     *
     * @param hostClass the host class from which log messages will be issued
     * @return a Logger
     */
    public static Logger getLogger(Class<?> hostClass) {
        return new SQLiteLogger(hostClass);
    }

    private static class SQLiteLogger implements Logger {
        final java.lang.System.Logger logger;

        public SQLiteLogger(Class<?> hostClass) {
            logger = System.getLogger(hostClass.getCanonicalName());
        }

        @Override
        public void trace(Supplier<String> message) {
            if (logger.isLoggable(Level.TRACE)) {
                logger.log(Level.TRACE, message.get());
            }
        }

        @Override
        public void info(Supplier<String> message) {
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, message.get());
            }
        }

        @Override
        public void warn(Supplier<String> message) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, message.get());
            }
        }

        @Override
        public void error(Supplier<String> message, Throwable t) {
            if (logger.isLoggable(Level.ERROR)) {
                logger.log(Level.ERROR, message.get(), t);
            }
        }
    }

}
