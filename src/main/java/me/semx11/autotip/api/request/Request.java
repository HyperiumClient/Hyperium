package me.semx11.autotip.api.request;

import me.semx11.autotip.api.RequestType;
import me.semx11.autotip.api.reply.Reply;

public interface Request<T extends Reply> {

    T execute();

    RequestType getType();

}
