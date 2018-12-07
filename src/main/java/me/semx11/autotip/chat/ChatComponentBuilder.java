package me.semx11.autotip.chat;

import me.semx11.autotip.universal.UniversalUtil;
import me.semx11.autotip.util.StringUtil;

public class ChatComponentBuilder {

    private static final String PREFIX = "&6A&eT &8> &7";

    private String text;
    private String hoverText;
    private String url;

    private ChatComponentBuilder(boolean prefix, String text, Object... params) {
        this.text = StringUtil.params((prefix ? PREFIX : "") + text, params);
    }

    public static ChatComponentBuilder of(String text, Object... params) {
        return new ChatComponentBuilder(true, text, params);
    }

    public static ChatComponentBuilder of(boolean prefix, String text, Object... params) {
        return new ChatComponentBuilder(prefix, text, params);
    }

    public ChatComponentBuilder setUrl(String url, Object... params) {
        this.url = StringUtil.params(url, false, params);
        return this;
    }

    public ChatComponentBuilder setHover(String hoverText, Object... params) {
        this.hoverText = StringUtil.params(hoverText, params);
        return this;
    }

    public void send() {
        UniversalUtil.addChatMessage(text, url, hoverText);
    }

}
