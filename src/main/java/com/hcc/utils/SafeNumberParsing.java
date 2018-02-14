package com.hcc.utils;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public final class SafeNumberParsing {

    public static int safeParseInt(String input, int fallback) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }
    public static int safeParseInt(String input) {
        return safeParseInt(input);
    }

}
