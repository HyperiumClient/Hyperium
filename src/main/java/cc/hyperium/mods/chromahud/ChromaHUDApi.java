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

package cc.hyperium.mods.chromahud;

import cc.hyperium.mods.chromahud.api.*;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author Sk1er
 */
public class ChromaHUDApi {
    // Have this for others incase implementation changes.
    public static final String VERSION = "3.0-Hyperium";
    private static ChromaHUDApi instance;
    private final List<ChromaHUDParser> parsers = new ArrayList<>();
    private final Map<String, String> names = new HashMap<>();
    private final List<DisplayElement> elements = new ArrayList<>();
    private final Map<String, ArrayList<ButtonConfig>> buttonConfigs = new HashMap<>();
    private final Map<String, ArrayList<TextConfig>> textConfigs = new HashMap<>();
    private final Map<String, ArrayList<StringConfig>> stringConfigs = new HashMap<>();
    private boolean posted;
    private JsonHolder config = new JsonHolder();

    private ChromaHUDApi() {
        instance = this;
    }

    /**
     * @return ChromaHUD Api Instance
     */
    public static ChromaHUDApi getInstance() {
        if (instance == null) instance = new ChromaHUDApi();
        return instance;
    }

    public List<ButtonConfig> getButtonConfigs(String type) {
        type = type.toLowerCase();
        List<ButtonConfig> configs = buttonConfigs.get(type);
        if (configs != null) return new ArrayList<>(configs);
        return new ArrayList<>();
    }

    public List<TextConfig> getTextConfigs(String type) {
        type = type.toLowerCase();
        List<TextConfig> configs = textConfigs.get(type);
        if (configs != null) return new ArrayList<>(configs);
        return new ArrayList<>();
    }

    public List<StringConfig> getStringConfigs(String type) {
        type = type.toLowerCase();
        List<StringConfig> configs = stringConfigs.get(type);
        if (configs != null) return new ArrayList<>(configs);
        return new ArrayList<>();
    }

    /**
     * @return All Display Elements the client is
     */
    public List<DisplayElement> getElements() {
        return elements;
    }

    /**
     * Register a text CONFIG
     *
     * @param type   String ID for StatsDisplayItem to show and activate on for CONFIG
     * @param config Config object
     */
    void registerTextConfig(String type, TextConfig config) {
        type = type.toLowerCase();
        if (!textConfigs.containsKey(type)) textConfigs.put(type, new ArrayList<>());
        textConfigs.get(type).add(config);
    }

    /**
     * Register a String CONFIG
     *
     * @param type   String ID for StatsDisplayItem to show and activate on for CONFIG
     * @param config Config object
     */
    void registerStringConfig(String type, StringConfig config) {
        type = type.toLowerCase();
        if (!stringConfigs.containsKey(type)) stringConfigs.put(type, new ArrayList<>());
        stringConfigs.get(type).add(config);
    }

    /**
     * Register a button CONFIG
     *
     * @param type   String ID for StatsDisplayItem to show and activate on for CONFIG
     * @param config Config object
     */
    void registerButtonConfig(String type, ButtonConfig config) {
        type = type.toLowerCase();
        if (!buttonConfigs.containsKey(type)) buttonConfigs.put(type, new ArrayList<>());
        buttonConfigs.get(type).add(config);
    }

    /**
     * <p>Add a parser to the ChromaHUD runtime. Must be done before InitializationEvent event</p>
     *
     * @param parser A valid ChromaHUDParser object for an addon.
     */
    public void register(ChromaHUDParser parser) {
        assert !posted : "Cannot register parser after InitializationEvent";
        parsers.add(parser);
        names.putAll(parser.getNames());
    }

    /**
     * Internal method to setup system once all items have been registered
     *
     * @param config Config data from file
     */
    public void post(JsonHolder config) {
        this.config = config;
        posted = true;
        elements.clear();
        JsonArray displayElements = config.optJSONArray("elements");
        int bound = displayElements.size();
        for (int i = 0; i < bound; i++) {
            JsonHolder object = new JsonHolder(displayElements.get(i).getAsJsonObject());
            try {
                DisplayElement e = new DisplayElement(object);
                if (e.getDisplayItems().size() > 0) elements.add(e);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger("ChromaHUD").severe("A fatal error occurred while loading the display element " + object);
            }
        }

        if (!config.has("elements")) {
            // setup blank
        }
    }

    /**
     * Parse StatsDisplayItem from CONFIG
     *
     * @param type type of item
     * @param ord  ordinal inside element
     * @param item Other JSON data that is stored
     * @return StatsDisplayItem instance created, null if the system was unable to resolve type
     */
    public DisplayItem parse(String type, int ord, JsonHolder item) {
        return parsers.stream().map(parser -> parser.parse(type, ord, item)).filter(Objects::nonNull).findFirst().orElse(null);
    }

    /**
     * ID -> Human readable name for a StatsDisplayItem
     *
     * @param type type of StatsDisplayItem to retrieve name for
     * @return Display name of StatsDisplayItem
     */
    public String getName(String type) {
        return names.getOrDefault(type, type);
    }

    public List<ChromaHUDParser> getParsers() {
        return new ArrayList<>(parsers);
    }

    public JsonHolder getConfig() {
        return config;
    }
}
