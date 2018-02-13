package com.hcc.mods.chromahud;


import com.hcc.mods.chromahud.api.ChromaHUDDescription;
import com.hcc.mods.chromahud.api.ChromaHUDParser;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.mods.chromahud.displayitems.LocationDisplay;
import com.hcc.utils.JsonHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public class DefaultChromaHudParser implements ChromaHUDParser {
    private HashMap<String, String> names = new HashMap<>();

    public DefaultChromaHudParser() {
        names.put("LOCATION", "Location");
    }

    @Override
    public DisplayItem parse(String type, int ord, JsonHolder item) {
        switch (type) {

            case "LOCATION":
                return new LocationDisplay(item, ord);

        }
        return null;
    }

    @Override
    public Map<String, String> getNames() {

        return names;
    }

    @Override
    public ChromaHUDDescription description() {
        return new ChromaHUDDescription("DEFAULT", "1.0", "HCC", "Default Items in HCC.");
    }
}
