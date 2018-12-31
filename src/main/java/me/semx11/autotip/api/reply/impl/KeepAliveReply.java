package me.semx11.autotip.api.reply.impl;

import me.semx11.autotip.api.RequestType;
import me.semx11.autotip.api.reply.Reply;

public class KeepAliveReply extends Reply {

    public KeepAliveReply() {
    }

    public KeepAliveReply(boolean success) {
        super(success);
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.KEEP_ALIVE;
    }

}
