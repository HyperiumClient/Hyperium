package me.semx11.autotip.api.request.impl;

import java.util.Optional;
import me.semx11.autotip.api.GetBuilder;
import me.semx11.autotip.api.RequestHandler;
import me.semx11.autotip.api.RequestType;
import me.semx11.autotip.api.SessionKey;
import me.semx11.autotip.api.reply.Reply;
import me.semx11.autotip.api.reply.impl.TipReply;
import me.semx11.autotip.api.request.Request;
import org.apache.http.client.methods.HttpUriRequest;

public class TipRequest implements Request<TipReply> {

    private final SessionKey sessionKey;

    private TipRequest(SessionKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    public static TipRequest of(SessionKey sessionKey) {
        return new TipRequest(sessionKey);
    }

    @Override
    public TipReply execute() {
        HttpUriRequest request = GetBuilder.of(this)
                .addParameter("key", this.sessionKey)
                .build();

        Optional<Reply> optional = RequestHandler.getReply(this, request.getURI());
        return optional
                .map(reply -> (TipReply) reply)
                .orElseGet(TipReply::getDefault);
    }

    @Override
    public RequestType getType() {
        return RequestType.TIP;
    }

}
