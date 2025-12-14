package dev.mockboard.core.utils;

import dev.mockboard.Constants;

import java.security.SecureRandom;

public final class SessionIdGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    private SessionIdGenerator() {}

    public static String generate() {
        var builder = new StringBuilder();
        for (int i = 0; i < Constants.SESSION_ID_LENGTH; i++) {
            int randomIdx = random.nextInt(CHARACTERS.length());
            builder.append(CHARACTERS.charAt(randomIdx));
        }
        return builder.toString();
    }
}
