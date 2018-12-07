package me.semx11.autotip.api.request.impl;

import java.util.Optional;
import me.semx11.autotip.api.GetBuilder;
import me.semx11.autotip.api.RequestHandler;
import me.semx11.autotip.api.RequestType;
import me.semx11.autotip.api.SessionKey;
import me.semx11.autotip.api.reply.Reply;
import me.semx11.autotip.api.reply.impl.LogoutReply;
import me.semx11.autotip.api.request.Request;
import org.apache.http.client.methods.HttpUriRequest;

public class LogoutRequest implements Request<LogoutReply> {

    private final SessionKey sessionKey;

    private LogoutRequest(SessionKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    public static LogoutRequest of(SessionKey sessionKey) {
        return new LogoutRequest(sessionKey);
    }

    @Override
    public LogoutReply execute() {
        HttpUriRequest request = GetBuilder.of(this)
                .addParameter("key", this.sessionKey)
                .build();

        Optional<Reply> optional = RequestHandler.getReply(this, request.getURI());
        return optional
                .map(reply -> (LogoutReply) reply)
                .orElseGet(() -> new LogoutReply(false));
    }

    @Override
    public RequestType getType() {
        return RequestType.LOGOUT;
    }

}
