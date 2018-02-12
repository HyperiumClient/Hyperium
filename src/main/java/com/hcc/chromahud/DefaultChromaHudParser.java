package com.hcc.chromahud;


import com.hcc.chromahud.api.ChromaHUDDescription;
import com.hcc.chromahud.api.ChromaHUDParser;
import com.hcc.chromahud.api.DisplayItem;
import com.hcc.chromahud.displayitems.FPS;
import com.hcc.utils.JsonHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public class DefaultChromaHudParser implements ChromaHUDParser {
    private HashMap<String, String> names = new HashMap<>();

    public DefaultChromaHudParser() {
        names.put("CORDS", "Cords");
        names.put("TEXT", "Text");
        names.put("ARMOUR_HUD", "Armour");
        names.put("POTION", "Potion");
        names.put("FPS", "FPS");
        names.put("PING", "Ping");
        names.put("DIRECTION", "Direction");
        names.put("CPS", "CPS");
        names.put("ARROW_COUNT", "Arrow Count");
        names.put("TIME", "Time");
        names.put("C_COUNTER", "C Counter");
    }

    @Override
    public DisplayItem parse(String type, int ord, JsonHolder item) {
        switch (type) {

            case "FPS":
                return new FPS(item, ord);

        }
        return null;
    }

    @Override
    public Map<String, String> getNames() {

        return names;
    }

    @Override
    public ChromaHUDDescription description() {
        return new ChromaHUDDescription("DEFAULT", "3.0", "ChromaHUD", "Default display items for ChromaHUD.");
    }
}
