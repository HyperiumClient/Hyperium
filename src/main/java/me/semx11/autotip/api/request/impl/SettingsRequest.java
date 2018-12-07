package me.semx11.autotip.api.request.impl;

import java.util.Optional;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.api.GetBuilder;
import me.semx11.autotip.api.RequestHandler;
import me.semx11.autotip.api.RequestType;
import me.semx11.autotip.api.reply.Reply;
import me.semx11.autotip.api.reply.impl.SettingsReply;
import me.semx11.autotip.api.request.Request;
import me.semx11.autotip.util.Version;
import org.apache.http.client.methods.HttpUriRequest;

public class SettingsRequest implements Request<SettingsReply> {

    private final Version version;

    private SettingsRequest(Autotip autotip) {
        this.version = autotip.getVersion();
    }

    public static SettingsRequest of(Autotip autotip) {
        return new SettingsRequest(autotip);
    }

    @Override
    public SettingsReply execute() {
        HttpUriRequest request = GetBuilder.of(this)
                .addParameter("v", this.version.get())
                .build();

        Optional<Reply> optional = RequestHandler.getReply(this, request.getURI());
        return optional
                .map(reply -> (SettingsReply) reply)
                .orElseGet(() -> new SettingsReply(false));
    }

    @Override
    public RequestType getType() {
        return RequestType.SETTINGS;
    }

}
