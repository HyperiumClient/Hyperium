/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.levelhead.util;

import com.google.gson.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class LevelheadJsonHolder {

    private JsonObject object;

    public LevelheadJsonHolder(JsonObject object) {
        this.object = object;
    }

    public LevelheadJsonHolder(String raw) {
        this(new JsonParser().parse(raw).getAsJsonObject());
    }

    public LevelheadJsonHolder() {
        this(new JsonObject());
    }

    public static String[] getNames(LevelheadJsonHolder statsObject) {
        List<String> keys = statsObject.getKeys();
        return keys.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return object.toString();
    }

    public LevelheadJsonHolder put(String key, boolean value) {
        object.addProperty(key, value);
        return this;
    }

    public void mergeNotOverride(LevelheadJsonHolder merge) {
        merge(merge, false);
    }

    public void mergeOverride(LevelheadJsonHolder merge) {
        merge(merge, true);
    }

    public void merge(LevelheadJsonHolder merge, boolean override) {
        JsonObject object = merge.object;
        merge.getKeys().stream().filter(s -> override || !has(s)).forEach(s -> put(s, object.get(s)));
    }

    public LevelheadJsonHolder put(String key, String value) {
        object.addProperty(key, value);
        return this;
    }

    public LevelheadJsonHolder put(String key, int value) {
        object.addProperty(key, value);
        return this;
    }

    public LevelheadJsonHolder put(String key, double value) {
        object.addProperty(key, value);
        return this;
    }

    public LevelheadJsonHolder put(String key, long value) {
        object.addProperty(key, value);
        return this;
    }

    public LevelheadJsonHolder optJsonObject(String key, JsonObject fallBack) {
        try {
            return new LevelheadJsonHolder(object.get(key).getAsJsonObject());
        } catch (Exception e) {
            return new LevelheadJsonHolder(fallBack);
        }
    }

    public LevelheadJsonHolder optJsonObject(String key, LevelheadJsonHolder fallBack) {
        try {
            return new LevelheadJsonHolder(object.get(key).getAsJsonObject());
        } catch (Exception e) {
            return fallBack;
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

    public JsonObject optActualJsonObject(String key) {
        try {
            return object.get(key).getAsJsonObject();
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    public LevelheadJsonHolder optJsonObject(String key) {
        return optJsonObject(key, new JsonObject());
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

    public LevelheadJsonHolder put(String values, LevelheadJsonHolder values1) {
        put(values, values1.object);
        return this;
    }

    public void put(String values, JsonObject object) {
        this.object.add(values, object);
    }


    public void remove(String header) {
        object.remove(header);
    }

    public String getString(String seed) {
        return optString(seed);
    }

    public JsonArray optJSONArrayNonNull(String matches) {
        return optJSONArray(matches);
    }

    public int getInt(String type) {
        return optInt(type);
    }

    public UUID getUUID(String offender) {
        return optUUID(offender);
    }

    private UUID optUUID(String offender) {
        return UUID.fromString(optString(offender));
    }

    public String getOptString(String removedBy) {
        return optString(removedBy, null);
    }

    public long getLong(String dateIssued) {
        return optLong(dateIssued);
    }

    public long getOptLong(String removeDate, long l) {
        return optLong(removeDate, l);
    }

    public Object get(String field) {
        return object.get(field);
    }

    public void put(String key, JsonArray jsonArrayHolder) {
        object.add(key, jsonArrayHolder);
    }

    public LevelheadJsonHolder put(String key, JsonElement element) {
        object.add(key, element);
        return this;
    }

    public LevelheadJsonHolder put(String key, Object value) {
        return put(key, value.toString());
    }

    public LevelheadJsonHolder put(String key, Number value) {
        object.add(key, new JsonPrimitive(value));
        return this;
    }

    public LevelheadJsonHolder getJsonHolder(String entry) {
        return optJsonObject(entry);
    }

    public void putNull(String key) {
        object.add(key, JsonNull.INSTANCE);
    }

    public JsonElement getElement(String field) {
        return object.get(field);
    }
}
