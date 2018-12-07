package me.semx11.autotip.api.request.impl;

import java.util.Optional;
import me.semx11.autotip.api.GetBuilder;
import me.semx11.autotip.api.RequestHandler;
import me.semx11.autotip.api.RequestType;
import me.semx11.autotip.api.SessionKey;
import me.semx11.autotip.api.reply.Reply;
import me.semx11.autotip.api.reply.impl.KeepAliveReply;
import me.semx11.autotip.api.request.Request;
import org.apache.http.client.methods.HttpUriRequest;

public class KeepAliveRequest implements Request<KeepAliveReply> {

    private final SessionKey sessionKey;

    private KeepAliveRequest(SessionKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    public static KeepAliveRequest of(SessionKey sessionKey) {
        return new KeepAliveRequest(sessionKey);
    }

    @Override
    public KeepAliveReply execute() {
        HttpUriRequest request = GetBuilder.of(this)
                .addParameter("key", this.sessionKey)
                .build();

        Optional<Reply> optional = RequestHandler.getReply(this, request.getURI());
        return optional
                .map(reply -> (KeepAliveReply) reply)
                .orElseGet(() -> new KeepAliveReply(false));
    }

    @Override
    public RequestType getType() {
        return RequestType.KEEP_ALIVE;
    }

}
