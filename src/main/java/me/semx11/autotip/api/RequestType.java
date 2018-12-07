package me.semx11.autotip.api;

import me.semx11.autotip.api.reply.impl.KeepAliveReply;
import me.semx11.autotip.api.reply.impl.LocaleReply;
import me.semx11.autotip.api.reply.impl.LoginReply;
import me.semx11.autotip.api.reply.impl.LogoutReply;
import me.semx11.autotip.api.reply.impl.SettingsReply;
import me.semx11.autotip.api.reply.impl.TipReply;

public enum RequestType {
    SETTINGS("settings", SettingsReply.class),
    LOCALE("locale", LocaleReply.class),
    LOGIN("login", LoginReply.class),
    KEEP_ALIVE("keepalive", KeepAliveReply.class),
    TIP("tip", TipReply.class),
    LOGOUT("logout", LogoutReply.class);

    private final String endpoint;
    private final Class<?> replyClass;

    RequestType(String endpoint, Class<?> replyClass) {
        this.endpoint = endpoint;
        this.replyClass = replyClass;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Class<?> getReplyClass() {
        return replyClass;
    }

}
