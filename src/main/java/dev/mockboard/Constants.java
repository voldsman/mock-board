package dev.mockboard;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String SESSION_COOKIE_NAME = "MOCK_BOARD_ID";
    public static final int SESSION_ID_LENGTH = 10;
    public static final int COOKIE_MAX_AGE = 60 * 60;

    public static final String MOCK_PATH_PREFIX = "/m";

    public static final int MAX_RECENT_REQUESTS = 20;

}
