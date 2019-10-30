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

package cc.hyperium.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The solution to the missing methods in Google's implementation
 * of json, this class contains many useful methods such as pretty
 * printing and file writing, as well as optional methods
 *
 * @author boomboompower
 * @version 1.0
 */
@SuppressWarnings({"WeakerAccess", "ResultOfMethodCallIgnored", "UnusedReturnValue"}) // Don't care
public class BetterJsonObject {

    /**
     * Our pretty printer
     */
    private final Gson prettyPrinter = new GsonBuilder().setPrettyPrinting().create();

    /**
     * The data holder for our json
     */
    private JsonObject data;

    /**
     * The quickest BetterJsonObject constructor, because why not
     */
    public BetterJsonObject() {
        data = new JsonObject();
    }

    /**
     * The default constructor the BetterJsonObject class, uses a json string
     * as the parameter and attempts to load it, a new JsonObject will be
     * created if the input is null, empty or cannot be loaded into a json format
     *
     * @param jsonIn the json string to be parsed
     */
    public BetterJsonObject(String jsonIn) {
        if (jsonIn == null || jsonIn.isEmpty()) {
            data = new JsonObject();
            return;
        }
        try {
            data = new JsonParser().parse(jsonIn).getAsJsonObject();
        } catch (JsonSyntaxException | JsonIOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The alternative constructor for the BetterJsonObject class, this uses
     * another JsonObject as the data set. A new JsonObject will be created
     * if the input is null
     *
     * @param objectIn the object to be used
     */
    public BetterJsonObject(JsonObject objectIn) {
        data = objectIn != null ? objectIn : new JsonObject();
    }

    /**
     * The optional string method, returns an empty string if
     * the key is null, empty or the data does not contain
     * the key. This will also return an empty string if the data value
     * is not a string
     *
     * @param key the key the value will be loaded from
     * @return the value in the json data set or empty if the key cannot be found
     */
    public String optString(String key) {
        return optString(key, "");
    }

    /**
     * The optional string method, returns the default value if
     * the key is null, empty or the data does not contain
     * the key. This will also return the default value if
     * the data value is not a string
     *
     * @param key the key the value will be loaded from
     * @return the value in the json data set or the default if the key cannot be found
     */
    public String optString(String key, String value) {
        if (key == null || key.isEmpty() || !has(key)) return value;
        JsonPrimitive primitive = asPrimitive(get(key));
        return primitive != null && primitive.isString() ? primitive.getAsString() : value;
    }

    /**
     * The optional int method, returns 0 if
     * the key is null, empty or the data does not contain
     * the key. This will also return 0 if the data value
     * is not a string
     *
     * @param key the key the value will be loaded from
     * @return the value in the json data set or empty if the key cannot be found
     */
    public int optInt(String key) {
        return optInt(key, 0);
    }

    /**
     * The optional int method, returns the default value if
     * the key is null, empty or the data does not contain
     * the key. This will also return the default value if
     * the data value is not a number
     *
     * @param key the key the value will be loaded from
     * @return the value in the json data set or the default if the key cannot be found
     */
    public int optInt(String key, int value) {
        if (key == null || key.isEmpty() || !has(key)) return value;

        JsonPrimitive primitive = asPrimitive(get(key));

        try {
            if (primitive != null && primitive.isNumber()) return primitive.getAsInt();
        } catch (NumberFormatException ignored) {
        }

        return value;
    }

    /**
     * The optional double method, returns 0.0D if
     * the key is null, empty or the data does not contain
     * the key. This will also return 0.0D if the data value
     * is not a string
     *
     * @param key the key the value will be loaded from
     * @return the value in the json data set or empty if the key cannot be found
     */
    public double optDouble(String key) {
        return optDouble(key, 0);
    }

    /**
     * The optional double method, returns the default value if
     * the key is null, empty or the data does not contain
     * the key. This will also return the default value if
     * the data value is not a number
     *
     * @param key the key the value will be loaded from
     * @return the value in the json data set or the default if the key cannot be found
     */
    public double optDouble(String key, double value) {
        if (key == null || key.isEmpty() || !has(key)) return value;

        JsonPrimitive primitive = asPrimitive(get(key));

        try {
            if (primitive != null && primitive.isNumber()) return primitive.getAsDouble();
        } catch (NumberFormatException ignored) {
        }

        return value;
    }

    /**
     * The optional boolean method, returns false if
     * the key is null, empty or the data does not contain
     * the key. This will also return false if the data value
     * is not a string
     *
     * @param key the key the value will be loaded from
     * @return the value in the json data set or empty if the key cannot be found
     */
    public boolean optBoolean(String key) {
        return optBoolean(key, false);
    }

    /**
     * The optional boolean method, returns the default value if
     * the key is null, empty or the data does not contain
     * the key. This will also return the default value if
     * the data value is not a boolean
     *
     * @param key the key the value will be loaded from
     * @return the value in the json data set or the default if the key cannot be found
     */
    public boolean optBoolean(String key, boolean value) {
        if (key == null || key.isEmpty() || !has(key)) return value;
        JsonPrimitive primitive = asPrimitive(get(key));
        return primitive != null && primitive.isBoolean() ? primitive.getAsBoolean() : value;
    }

    public boolean has(String key) {
        return data.has(key);
    }

    public JsonElement get(String key) {
        return data.get(key);
    }

    /**
     * Returns the data the information is being loaded from
     *
     * @return the loading data
     */
    public JsonObject getData() {
        return data;
    }

    /**
     * Adds a string to the to the json data file with the
     * key that it'll be associated with
     *
     * @param key   the key
     * @param value the value
     */
    public BetterJsonObject addProperty(String key, String value) {
        if (key != null) data.addProperty(key, value);
        return this;
    }

    /**
     * Adds a number to the to the json data file with the
     * key that it'll be associated with
     *
     * @param key   the key
     * @param value the value
     */
    public BetterJsonObject addProperty(String key, Number value) {
        if (key != null) data.addProperty(key, value);
        return this;
    }

    /**
     * Adds a boolean to the to the json data file with the
     * key that it'll be associated with
     *
     * @param key   the key
     * @param value the value
     */
    public BetterJsonObject addProperty(String key, Boolean value) {
        if (key != null) data.addProperty(key, value);
        return this;
    }

    /**
     * Adds another BetterJsonObject into this one
     *
     * @param key    the key
     * @param object the object to add
     */
    public BetterJsonObject add(String key, BetterJsonObject object) {
        if (key != null) data.add(key, object.data);
        return this;
    }

    /**
     * This feature is a HUGE WIP and may not work, it is safer
     * to use the toString method with a BufferedWriter instead
     * <p>
     * We are not responsible for any overwritten files, please use this carefully
     *
     * @param file File to write to
     * @apiNote Use with caution, we are not responsible for you breaking files
     */
    public void writeToFile(File file) {
        // Do nothing if future issues may occur
        if (file == null || (file.exists() && file.isDirectory())) return;

        try {
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) parent.mkdirs();
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(toPrettyString()); // Use our pretty printer
            bufferedWriter.close(); // Close the BufferedWriter
            writer.close(); // Close the FileWriter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the JsonElement to the JsonPrimitive class to allow for better
     * functionality
     *
     * @param element the element to be transferred
     * @return the JsonPrimitive instance or null if is not an instanceof the JsonPrimitive class
     */
    private JsonPrimitive asPrimitive(JsonElement element) {
        return element instanceof JsonPrimitive ? (JsonPrimitive) element : null;
    }

    /**
     * Returns the data values toString method
     *
     * @return the data values toString
     */
    @Override
    public String toString() {
        return data.toString();
    }

    /**
     * Returns the pretty printed data String with
     * indents and other things
     *
     * @return pretty printed data
     */
    public String toPrettyString() {
        return prettyPrinter.toJson(data);
    }
}
