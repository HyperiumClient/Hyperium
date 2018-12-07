package me.semx11.autotip.gson.adapter.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.semx11.autotip.api.SessionKey;
import me.semx11.autotip.gson.adapter.TypeAdapter;

public class SessionKeyAdapter implements TypeAdapter<SessionKey> {

    @Override
    public SessionKey deserialize(JsonElement json) {
        return new SessionKey(json.getAsString());
    }

    @Override
    public JsonElement serialize(SessionKey src) {
        return new JsonPrimitive(src.getKey());
    }

}
