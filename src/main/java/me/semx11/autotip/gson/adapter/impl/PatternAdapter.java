package me.semx11.autotip.gson.adapter.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.regex.Pattern;
import me.semx11.autotip.gson.adapter.TypeAdapter;

public class PatternAdapter implements TypeAdapter<Pattern> {

    @Override
    public Pattern deserialize(JsonElement json) {
        return Pattern.compile(json.getAsString());
    }

    @Override
    public JsonElement serialize(Pattern src) {
        return new JsonPrimitive(src.pattern());
    }

}
