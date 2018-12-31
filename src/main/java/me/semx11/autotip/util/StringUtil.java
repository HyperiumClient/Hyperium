package me.semx11.autotip.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern FORMAT_PATTERN = Pattern.compile("(?im)&([0-9A-FK-OR])");
    private static final Pattern PARAM_PATTERN = Pattern.compile("\\{}");

    public static String params(String input, Object... params) {
        return params(input, true, params);
    }

    public static String params(String input, boolean color, Object... params) {
        if (color) {
            input = format(input);
        }
        if (params == null) {
            return input;
        }
        for (Object o : params) {
            if (o != null) {
                String replacement = Matcher.quoteReplacement(format(o.toString()));
                input = PARAM_PATTERN.matcher(input).replaceFirst(replacement);
            }
        }
        return input;
    }

    public static String format(String msg) {
        return msg.contains("&") ? FORMAT_PATTERN.matcher(msg).replaceAll("\u00a7$1") : msg;
    }

}
