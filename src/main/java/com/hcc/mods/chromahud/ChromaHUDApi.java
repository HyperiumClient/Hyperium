/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.mods.chromahud;

import com.google.gson.JsonArray;
import com.hcc.mods.chromahud.api.*;
import com.hcc.utils.JsonHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public class ChromaHUDApi {
    //Have this for others incase implementation changes.
    public static final String VERSION = "3.0-HCC";
    private static ChromaHUDApi instance;
    private List<ChromaHUDParser> parsers = new ArrayList<>();
    private HashMap<String, String> names = new HashMap<>();
    private List<DisplayElement> elements = new ArrayList<>();
    private boolean posted = false;
    private HashMap<String, ArrayList<ButtonConfig>> buttonConfigs = new HashMap<>();
    private HashMap<String, ArrayList<TextConfig>> textConfigs = new HashMap<>();
    private HashMap<String, ArrayList<StringConfig>> stringConfigs = new HashMap<>();

    private ChromaHUDApi() {
        instance = this;
    }

    /**
     * @return ChromaHUD Api Instance
     */
    public static ChromaHUDApi getInstance() {
        if (instance == null)
            instance = new ChromaHUDApi();
        return instance;
    }

    public List<ButtonConfig> getButtonConfigs(String type) {
        type = type.toLowerCase();
        ArrayList<ButtonConfig> configs = buttonConfigs.get(type);
        if (configs != null)
            return new ArrayList<>(configs);
        return new ArrayList<>();
    }

    public List<TextConfig> getTextConfigs(String type) {
        type = type.toLowerCase();
        ArrayList<TextConfig> configs = this.textConfigs.get(type);
        if (configs != null)
            return new ArrayList<>(configs);
        return new ArrayList<>();
    }

    public List<StringConfig> getStringConfigs(String type) {
        type = type.toLowerCase();
        ArrayList<StringConfig> configs = this.stringConfigs.get(type);
        if (configs != null)
            return new ArrayList<>(configs);
        return new ArrayList<>();
    }

    /**
     * @return All Display Elements the client is
     */
    public List<DisplayElement> getElements() {
        return elements;
    }

    /**
     * Register a text config
     *
     * @param type   String ID for DisplayItem to show and activate on for config
     * @param config Config object
     */
    public void registerTextConfig(String type, TextConfig config) {
        type = type.toLowerCase();
        if (!textConfigs.containsKey(type))
            textConfigs.put(type, new ArrayList<>());
        textConfigs.get(type).add(config);
    }

    /**
     * Register a String config
     *
     * @param type   String ID for DisplayItem to show and activate on for config
     * @param config Config object
     */
    public void registerStringConfig(String type, StringConfig config) {
        type = type.toLowerCase();
        if (!stringConfigs.containsKey(type))
            stringConfigs.put(type, new ArrayList<>());
        stringConfigs.get(type).add(config);
    }

    /**
     * Register a button config
     *
     * @param type   String ID for DisplayItem to show and activate on for config
     * @param config Config object
     */
    public void registerButtonConfig(String type, ButtonConfig config) {
        type = type.toLowerCase();
        if (!buttonConfigs.containsKey(type))
            buttonConfigs.put(type, new ArrayList<>());
        buttonConfigs.get(type).add(config);
    }

    /**
     * <p>Add a parser to the ChromaHUD runtime. Must be done before FMLPostInitialization event</p>
     *
     * @param parser A valid ChromaHUDParser object for an addon.
     */
    public void register(ChromaHUDParser parser) {
        if (posted)
            throw new IllegalStateException("Cannot register parser after FMLPostInitialization event");
        parsers.add(parser);
        names.putAll(parser.getNames());
    }

    /**
     * Internal method to setup system once all items have been registered
     *
     * @param config Config data from file
     */
    protected void post(JsonHolder config) {
        if (posted)
            throw new IllegalStateException("Already posted!");
        this.posted = true;
        elements.clear();
        JsonArray displayElements = config.optJSONArray("elements");
        for (int i = 0; i < displayElements.size(); i++) {
            JsonHolder object = new JsonHolder(displayElements.get(i).getAsJsonObject());
            try {
                DisplayElement e = new DisplayElement(object);
                if (e.getDisplayItems().size() > 0)
                    elements.add(e);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger("ChromaHUD").severe("A fatal error occurred while loading the display element " + object);
            }
        }
        if (!config.has("elements")) {
           //setup blank
        }

    }

    /**
     * Parse DisplayItem from config
     *
     * @param type type of item
     * @param ord  ordinal inside element
     * @param item Other JSON data that is stored
     * @return DisplayItem instance created, null if the system was unable to resolve type
     */
    public DisplayItem parse(String type, int ord, JsonHolder item) {
        for (ChromaHUDParser parser : parsers) {
            DisplayItem parsed = parser.parse(type, ord, item);
            if (parsed != null) {

                return parsed;
            }
        }
        //No parsers could parse -> return null
        return null;
    }

    /**
     * ID -> Human readable name for a DisplayItem
     *
     * @param type type of DisplayItem to retrieve name for
     * @return Display name of DisplayItem
     */
    public String getName(String type) {
        return names.getOrDefault(type, type);
    }

    public List<ChromaHUDParser> getParsers() {
        return new ArrayList<>(parsers);
    }
}
