/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sk1er
 */
@SuppressWarnings("unused")
public class JsonHolder {
    private JsonObject object = new JsonObject();
    private boolean parsedCorrectly = true;

    public JsonHolder(JsonObject object) {
        this.object = object;
    }

    public JsonHolder(String raw) {
        if (raw == null || raw.isEmpty()) {
            object = new JsonObject();
            return;
        }
        try {
            this.object = new JsonParser().parse(raw).getAsJsonObject();
        } catch (Exception e) {
            this.object = new JsonObject();
            this.parsedCorrectly = false;
            e.printStackTrace();

        }
    }

    public JsonHolder() {
        this(new JsonObject());
    }

    public boolean isParsedCorrectly() {
        return parsedCorrectly;
    }

    @Override
    public String toString() {
        if (object != null)
            return object.toString();
        return "{}";
    }

    public JsonHolder put(String key, boolean value) {
        object.addProperty(key, value);
        return this;
    }

    public void mergeNotOverride(JsonHolder merge) {
        merge(merge, false);
    }

    public void mergeOverride(JsonHolder merge) {
        merge(merge, true);
    }

    public void merge(JsonHolder merge, boolean override) {
        JsonObject object = merge.getObject();
        for (String s : merge.getKeys()) {
            if (override || !this.has(s))
                put(s, object.get(s));
        }
    }

    private void put(String s, JsonElement element) {
        this.object.add(s, element);
    }

    public JsonHolder put(String key, String value) {
        object.addProperty(key, value);
        return this;
    }

    public JsonHolder put(String key, int value) {
        object.addProperty(key, value);
        return this;
    }

    public JsonHolder put(String key, double value) {
        object.addProperty(key, value);
        return this;
    }

    public JsonHolder put(String key, long value) {
        object.addProperty(key, value);
        return this;
    }

    public JsonHolder optJSONObject(String key, JsonObject fallBack) {
        try {
            return new JsonHolder(object.get(key).getAsJsonObject());
        } catch (Exception e) {
            return new JsonHolder(fallBack);
        }
    }

    public JsonArray optJSONArray(String key, JsonArray fallback) {
        try {
            return object.get(key).getAsJsonArray();
        } catch (Exception e) {
            return fallback;
        }
    }

    public JsonArray optJSONArray(String key) {
        return optJSONArray(key, new JsonArray());
    }


    public boolean has(String key) {
        return object.has(key);
    }

    public long optLong(String key, long fallback) {
        try {
            return object.get(key).getAsLong();
        } catch (Exception e) {
            return fallback;
        }
    }

    public long optLong(String key) {
        return optLong(key, 0);
    }

    public boolean optBoolean(String key, boolean fallback) {
        try {
            return object.get(key).getAsBoolean();
        } catch (Exception e) {
            return fallback;
        }
    }

    public boolean optBoolean(String key) {
        return optBoolean(key, false);
    }

    public JsonObject optActualJSONObject(String key) {
        try {
            return object.get(key).getAsJsonObject();
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    public JsonHolder optJSONObject(String key) {
        return optJSONObject(key, new JsonObject());
    }


    public int optInt(String key, int fallBack) {
        try {
            return object.get(key).getAsInt();
        } catch (Exception e) {
            return fallBack;
        }
    }

    public int optInt(String key) {
        return optInt(key, 0);
    }


    public String optString(String key, String fallBack) {
        try {
            return object.get(key).getAsString();
        } catch (Exception e) {
            return fallBack;
        }
    }

    public String optString(String key) {
        return optString(key, "");
    }


    public double optDouble(String key, double fallBack) {
        try {
            return object.get(key).getAsDouble();
        } catch (Exception e) {
            return fallBack;
        }
    }

    //modgame add bw_4 2 4 `Bedwars 4 Team` Bedwars
    //modgame rm Bedwars
    public List<String> getKeys() {
        return object.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public double optDouble(String key) {
        return optDouble(key, 0.0);
    }


    public JsonObject getObject() {
        return object;
    }

    public boolean isNull(String key) {
        return object.has(key) && object.get(key).isJsonNull();
    }

    public JsonHolder put(String values, JsonHolder values1) {
        return put(values, values1.getObject());
    }

    public JsonHolder put(String values, JsonObject object) {
        this.object.add(values, object);
        return this;
    }

    public void put(String blacklisted, JsonArray jsonElements) {
        this.object.add(blacklisted, jsonElements);
    }

    public void remove(String header) {
        object.remove(header);
    }

    public List<String> getJsonArrayAsStringList(String root) {
        List<String> strings = new ArrayList<>();
        try {

            for (JsonElement element : object.get(root).getAsJsonArray()) {
                strings.add(element.getAsString());
            }
        } catch (Exception ignored) {

        }
        return strings;
    }
}