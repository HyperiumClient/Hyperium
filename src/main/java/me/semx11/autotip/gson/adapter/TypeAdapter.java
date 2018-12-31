package me.semx11.autotip.gson.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public interface TypeAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    default T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return this.deserialize(json);
    }

    @Override
    default JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return this.serialize(src);
    }

    T deserialize(JsonElement json);

    JsonElement serialize(T src);

}
