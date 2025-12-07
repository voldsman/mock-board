package dev.mockboard.config;

public final class AppConfig {

    private AppConfig() {}

    public static final long MAX_REQUEST_BODY_SIZE = (long) 1024 * 1024; // 1MB
    public static final int MAX_HISTORY_PER_SESSION = 20;
    public static final int SESSION_TIMEOUT_MINUTES = 60;

    // Server Config
    public static final int PORT = 8080;
    public static final boolean DEV_MODE = true;
}
