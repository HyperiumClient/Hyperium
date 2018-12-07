package me.semx11.autotip.api.reply.impl;

import com.google.gson.JsonObject;
import java.util.Locale;
import me.semx11.autotip.api.RequestType;
import me.semx11.autotip.api.reply.Reply;
import me.semx11.autotip.chat.LocaleHolder;

public class LocaleReply extends Reply {

    private Locale lang;
    private JsonObject locale;

    public LocaleReply() {
    }

    public LocaleReply(boolean success) {
        super(success);
    }

    public LocaleHolder getLocaleHolder() {
        return new LocaleHolder(lang, locale);
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.LOCALE;
    }

}
