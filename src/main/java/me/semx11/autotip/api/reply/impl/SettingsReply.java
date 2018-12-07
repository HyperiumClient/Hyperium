package me.semx11.autotip.api.reply.impl;

import me.semx11.autotip.api.RequestType;
import me.semx11.autotip.api.reply.Reply;
import me.semx11.autotip.config.GlobalSettings;

public class SettingsReply extends Reply {

    private GlobalSettings settings;

    public SettingsReply() {
    }

    public SettingsReply(boolean success) {
        super(success);
    }

    public GlobalSettings getSettings() {
        return settings;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SETTINGS;
    }

}
