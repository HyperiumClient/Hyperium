package me.semx11.autotip.chat;

import java.util.Arrays;

public enum MessageOption {
    DEBUG("&6Debug"),
    SHOWN("&aShown"),
    COMPACT("&eCompact"),
    HIDDEN("&cHidden");

    private final String format;

    MessageOption(String format) {
        this.format = format;
    }

    public MessageOption next() {
        switch (this) {
            case DEBUG:
                return SHOWN;
            case SHOWN:
                return COMPACT;
            case COMPACT:
                return HIDDEN;
            case HIDDEN:
                return SHOWN;
            default:
                return null;
        }
    }

    public static MessageOption valueOfIgnoreCase(String name) {
        return Arrays.stream(MessageOption.values())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {
        return format;
    }

}