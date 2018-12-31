package me.semx11.autotip.gson.adapter.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Locale;
import me.semx11.autotip.gson.adapter.TypeAdapter;

public class LocaleAdapter implements TypeAdapter<Locale> {

    @Override
    public Locale deserialize(JsonElement json) {
        return Locale.forLanguageTag(json.getAsString());
    }

    @Override
    public JsonElement serialize(Locale src) {
        return new JsonPrimitive(src.toLanguageTag());
    }

}
