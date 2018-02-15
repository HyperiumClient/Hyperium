package com.hcc.mods.chromahud;


import com.hcc.mods.chromahud.api.ChromaHUDDescription;
import com.hcc.mods.chromahud.api.ChromaHUDParser;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.mods.chromahud.displayitems.HypixelDisplay;
import com.hcc.mods.chromahud.displayitems.LocationDisplay;
import com.hcc.mods.chromahud.displayitems.RatingDisplay;
import com.hcc.utils.JsonHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public class HCCChromaHudParser implements ChromaHUDParser {
    private HashMap<String, String> names = new HashMap<>();

    public HCCChromaHudParser() {
        names.put("LOCATION", "Location");
        names.put("HYPIXEL", "Hypixel");
        names.put("RATING", "Rating");
    }

    @Override
    public DisplayItem parse(String type, int ord, JsonHolder item) {
        switch (type) {

            case "LOCATION":
                return new LocationDisplay(item, ord);
            case "HYPIXEL":
                return new HypixelDisplay(item, ord);
            case "RATING" :
                return new RatingDisplay(item,ord);

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
