package me.semx11.autotip.api.reply.impl;

import me.semx11.autotip.api.RequestType;
import me.semx11.autotip.api.SessionKey;
import me.semx11.autotip.api.reply.Reply;

public class LoginReply extends Reply {

    private SessionKey sessionKey;
    private long keepAliveRate;
    private long tipWaveRate;
    private long tipCycleRate;

    public LoginReply() {
    }

    public LoginReply(boolean success) {
        super(success);
    }

    public SessionKey getSessionKey() {
        return sessionKey;
    }

    public long getKeepAliveRate() {
        return keepAliveRate;
    }

    public long getTipWaveRate() {
        return tipWaveRate;
    }

    public long getTipCycleRate() {
        return tipCycleRate;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.LOGIN;
    }

}
