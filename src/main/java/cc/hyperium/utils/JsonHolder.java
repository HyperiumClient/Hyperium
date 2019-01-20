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


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A utility which wraps {@link JsonObject}
 *
 * @author Sk1er
 */
@SuppressWarnings("unused")
public class JsonHolder {

    private static final Gson GSON = new GsonBuilder().create();

    /**
     * The wrapped JsonObject
     */
    private JsonObject object;

    /**
     * Whether is the given JSON parsed correctly or not
     */
    private boolean parsedCorrectly = true;

    /**
     * Retrieves a JsonHolder from the given {@link JsonObject}
     *
     * @param object Object to wrap
     */
    public JsonHolder(JsonObject object) {
        this.object = object;
    }

    /**
     * Initiates a new JsonHolder from the given string
     *
     * @param raw JSON string
     */
    public JsonHolder(String raw) {
        if (raw == null || raw.isEmpty()) {
            object = new JsonObject();
            return;
        }
        try {
            this.object = GSON.fromJson(raw, JsonElement.class).getAsJsonObject();
        } catch (Exception e) {
            this.object = new JsonObject();
            this.parsedCorrectly = false;
            e.printStackTrace();
        }
    }

    /**
     * Initiates a new empty JsonHolder
     */
    public JsonHolder() {
        this(new JsonObject());
    }

    /**
     * Returns whether is the JSON parsed correctly
     *
     * @return Whether is the JSON parsed correctly
     */
    public boolean isParsedCorrectly() {
        return parsedCorrectly;
    }

    /**
     * Returns the JSON string of this object
     *
     * @return THe JSON string
     */
    @Override
    public String toString() {
        if (object != null)
            return object.toString();
        return "{}";
    }

    /**
     * Assigns a boolean value to the given key
     *
     * @param key   Key to assign to
     * @param value The boolean value
     * @return This holder instance
     */
    public JsonHolder put(String key, boolean value) {
        object.addProperty(key, value);
        return this;
    }

    /**
     * Merges 2 JsonHolders without overriding shared content
     *
     * @param merge Holder to merge
     */
    public void mergeNotOverride(JsonHolder merge) {
        merge(merge, false);
    }

    /**
     * Merges 2 JsonHolders and overrides all the shared content
     *
     * @param merge Holder to merge
     */
    public void mergeOverride(JsonHolder merge) {
        merge(merge, true);
    }

    /**
     * Merges 2 JsonHolders
     *
     * @param merge    Holder to merge
     * @param override Whether to override the mutual content or not
     */
    public void merge(JsonHolder merge, boolean override) {
        JsonObject object = merge.getObject();
        for (String s : merge.getKeys()) {
            if (override || !this.has(s))
                put(s, object.get(s));
        }
    }

    /**
     * Assigns the given JsonElement to the specified key
     *
     * @param key     Key to assign to
     * @param element Element to assign to the key
     */
    private void put(String key, JsonElement element) {
        object.add(key, element);
    }

    /**
     * Assigns the given string to the specified key
     *
     * @param key   Key to assign to
     * @param value String to assign to the key
     * @return This holder instance
     */
    public JsonHolder put(String key, String value) {
        object.addProperty(key, value);
        return this;
    }

    /**
     * Assigns the given integer to the specified key
     *
     * @param key   Key to assign to
     * @param value Integer to assign to the key
     * @return This holder instance
     */
    public JsonHolder put(String key, int value) {
        object.addProperty(key, value);
        return this;
    }

    /**
     * Assigns the given double to the specified key
     *
     * @param key   Key to assign to
     * @param value Double to assign to the key
     * @return This holder instance
     */
    public JsonHolder put(String key, double value) {
        object.addProperty(key, value);
        return this;
    }

    /**
     * Assigns the given long to the specified key
     *
     * @param key   Key to assign to
     * @param value Long to assign to the key
     * @return This holder instance
     */
    public JsonHolder put(String key, long value) {
        object.addProperty(key, value);
        return this;
    }


    /**
     * Adds the given JsonHolder and maps it to the specified key
     *
     * @param key   Key to assign to
     * @param value The JsonHolder to assign
     * @return This JsonHolder instance
     */
    public JsonHolder put(String key, JsonHolder value) {
        return put(key, value.getObject());
    }

    /**
     * Adds the given JsonObject and maps it to the specified key
     *
     * @param key    Key to assign to
     * @param object The object to assign
     * @return This JsonHolder instance
     */
    public JsonHolder put(String key, JsonObject object) {
        this.object.add(key, object);
        return this;
    }

    /**
     * Adds the given JsonArray and maps it to the specified key
     *
     * @param key   Key to assign to
     * @param value The JsonArray to assign
     * @return This JsonHolder instance
     */
    public JsonHolder put(String key, JsonArray value) {
        this.object.add(key, value);
        return this;
    }

    /**
     * Creates a holder from the given key retrieved from this object
     *
     * @param key      Key of the object
     * @param fallBack Object to return if it couldn't fetch from the given key
     * @return The new JsonHolder
     */
    public JsonHolder optJSONObject(String key, JsonObject fallBack) {
        try {
            return new JsonHolder(object.get(key).getAsJsonObject());
        } catch (Exception e) {
            return new JsonHolder(fallBack);
        }
    }

    /**
     * Opts a {@link JsonArray} from the given key, or returns the given array if not found
     *
     * @param key      Key to retrieve from
     * @param fallback Object to return if none was found
     * @return The JsonArray of the key
     */
    public JsonArray optJSONArray(String key, JsonArray fallback) {
        try {
            return object.get(key).getAsJsonArray();
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Opts a {@link JsonArray} from the given key, or returns an empty array if not found
     *
     * @param key Key to retrieve from
     * @return The JsonArray of the key
     */
    public JsonArray optJSONArray(String key) {
        return optJSONArray(key, new JsonArray());
    }

    /**
     * Returns the {@link JsonObject} assigned to the specified key, or returns an empty object if not found
     *
     * @param key Key to opt from
     * @return The JsonObject assigned to the key, or an empty object if not found
     */
    public JsonObject optActualJSONObject(String key) {
        try {
            return object.get(key).getAsJsonObject();
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    /**
     * Opts the JsonObject from the given key, or returns an empty object if not found
     *
     * @param key Key to opt from
     * @return The object assigned to the key, or a new one if not found
     */
    public JsonHolder optJSONObject(String key) {
        return optJSONObject(key, new JsonObject());
    }

    /**
     * Opts an integer from the given key, or returns the specified integer if not found
     *
     * @param key      Key to opt from
     * @param fallback Value to return if not found
     * @return The integer assigned to the object
     */
    public int optInt(String key, int fallback) {
        try {
            return object.get(key).getAsInt();
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Opts the integer from the given key, or returns 0 if not found
     *
     * @param key Key to opt from
     * @return The long assigned to the key, or 0 if not found
     */
    public int optInt(String key) {
        return optInt(key, 0);
    }

    /**
     * Opts a long from the given key, or returns the specified long if not found
     *
     * @param key      Key to opt from
     * @param fallback Value to return if not found
     * @return The long assigned to the object
     */
    public long optLong(String key, long fallback) {
        try {
            return object.get(key).getAsLong();
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Opts the long from the given key, or returns 0 if not found
     *
     * @param key Key to opt from
     * @return The long assigned to the key, or 0 if not found
     */
    public long optLong(String key) {
        return optLong(key, 0);
    }

    /**
     * Opts a double from the given key, or returns the specified double if not found
     *
     * @param key      Key to opt from
     * @param fallback Value to return if not found
     * @return The long assigned to the object
     */
    public double optDouble(String key, double fallback) {
        try {
            return object.get(key).getAsDouble();
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Opts the double from the given key, or returns 0 if not found
     *
     * @param key Key to opt from
     * @return The double assigned to the key, or 0 if not found
     */
    public double optDouble(String key) {
        return optDouble(key, 0.0);
    }

    /**
     * Opts a boolean from the given key, or returns the specified boolean if not found
     *
     * @param key      Key to opt from
     * @param fallback Value to return if not found
     * @return The boolean assigned to the object
     */
    public boolean optBoolean(String key, boolean fallback) {
        try {
            return object.get(key).getAsBoolean();
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Opts the boolean from the given key, or returns {@code false} if not found
     *
     * @param key Key to opt from
     * @return The boolean assigned to the key, or {@code false} if not found
     */
    public boolean optBoolean(String key) {
        return optBoolean(key, false);
    }

    /**
     * Opts a String from the given key, or returns the specified String if not found
     *
     * @param key      Key to opt from
     * @param fallback Value to return if not found
     * @return The String assigned to the object
     */
    public String optString(String key, String fallback) {
        try {
            return object.get(key).getAsString();
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Opts the String from the given key, or returns an empty string if not found
     *
     * @param key Key to opt from
     * @return The string assigned to the key, or an empty string if not found
     */
    public String optString(String key) {
        return optString(key, "");
    }

    /**
     * Returns whether the JsonObject contains the given key or not
     *
     * @param key Key to check
     * @return {@code true} if the object contains the key, false if otherwise
     */
    public boolean has(String key) {
        return object.has(key);
    }

    /**
     * Returns the wrapped object in this holder
     *
     * @return The object in this holder
     */
    public JsonObject getObject() {
        return object;
    }

    /**
     * Returns whether is the specified key is null. This returns {@code true} if the object doesn't
     * contain the given key
     *
     * @param key Key to check
     * @return True if the value of the key is null (or if the key doesn't exist)
     */
    public boolean isNull(String key) {
        return object.has(key) && object.get(key).isJsonNull();
    }

    /**
     * Removes the key and the value assigned to it
     *
     * @param key Key to remove
     */
    public void remove(String key) {
        object.remove(key);
    }

    /**
     * Returns a list of strings which contains the elements inside the JsonArray assigned to the specific key
     *
     * @param arrayKey Key of the array
     * @return The list which contains the elements
     */
    public List<String> getJsonArrayAsStringList(String arrayKey) {
        Type listType = new TypeToken<LinkedList<String>>() {
        }.getType();
        return GSON.fromJson(object.get(arrayKey).getAsJsonArray(), listType);
    }

    /**
     * Returns a list of string which contains all the keys of this holder
     *
     * @return A list of all the keys
     */
    public List<String> getKeys() {
        return object.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
