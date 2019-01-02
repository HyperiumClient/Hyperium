package me.semx11.autotip.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import me.semx11.autotip.util.ErrorReport;

public class LocaleHolder {

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\.");

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    private final Locale locale;
    private final JsonObject root;

    public LocaleHolder(Locale locale, JsonObject root) {
        this.locale = locale;
        this.root = root;
    }

    public Locale getLocale() {
        return locale;
    }

    public JsonObject getRoot() {
        return root;
    }

    public String getKey(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        String value = "<" + key + ">";
        try {
            value = this.resolveKey(key);
        } catch (IllegalArgumentException e) {
            ErrorReport.reportException(e);
        }
        cache.put(key, value);
        return value;
    }

    private String resolveKey(String key) {
        JsonObject obj = root;
        for (String path : SPLIT_PATTERN.split(key)) {
            if (!obj.has(path)) {
                throw new IllegalArgumentException("Invalid key: " + key);
            }
            JsonElement value = obj.get(path);
            if (value.isJsonObject()) {
                obj = value.getAsJsonObject();
                continue;
            }
            return value.getAsString();
        }
        if (!obj.isJsonPrimitive()) {
            throw new IllegalArgumentException("Incomplete key: " + key);
        }
        return obj.getAsString();
    }

}
