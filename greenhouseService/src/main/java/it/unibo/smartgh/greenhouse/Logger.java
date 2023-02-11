package it.unibo.smartgh.greenhouse;

/**
 * Utility class for logging Greenhouse service information.
 */
public class Logger {
    /**
     * Logger utility.
     * @param msg message.
     */
    public static void log(String msg) {
        System.out.println("[GreenhouseHTTPAdapter][" + System.currentTimeMillis() + "] " + msg);
    }
}
