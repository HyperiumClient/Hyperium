package me.semx11.autotip.chat;

import java.util.regex.Pattern;

public enum ChatColor {
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    OBFUSCATED('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINE('n'),
    ITALIC('o'),
    RESET('r');

    private static final Pattern PATTERN = Pattern.compile("(?i)\\u00a7[0-9A-FK-OR]");

    private char formattingCode;

    ChatColor(char formattingCode) {
        this.formattingCode = formattingCode;
    }

    @Override
    public String toString() {
        return "\u00a7" + formattingCode;
    }

    public static String stripFormatting(String text) {
        return text == null ? null : PATTERN.matcher(text).replaceAll("");
    }

}
