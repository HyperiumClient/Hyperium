package me.semx11.autotip.gson.adapter.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.semx11.autotip.gson.adapter.TypeAdapter;
import me.semx11.autotip.util.Version;

public class VersionAdapter implements TypeAdapter<Version> {

    @Override
    public Version deserialize(JsonElement json) {
        return new Version(json.getAsString());
    }

    @Override
    public JsonElement serialize(Version src) {
        return new JsonPrimitive(src.get());
    }

}
