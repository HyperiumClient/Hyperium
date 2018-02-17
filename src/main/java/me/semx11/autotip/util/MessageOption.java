package me.semx11.autotip.util;

import org.apache.commons.lang3.StringUtils;

public enum MessageOption {
    SHOWN, COMPACT, HIDDEN;

    public MessageOption next() {
        switch (this) {
            case SHOWN:
                return COMPACT;
            case COMPACT:
                return HIDDEN;
            case HIDDEN:
                return SHOWN;
            default:
                return SHOWN;
        }
    }

    @Override
    public String toString() {
        ChatColor color = ChatColor.GREEN;
        switch (this) {
            case COMPACT:
                color = ChatColor.YELLOW;
                break;
            case HIDDEN:
                color = ChatColor.RED;
                break;
        }
        return color + StringUtils.capitalize(this.name().toLowerCase());
    }
}