package cc.hyperium.mods.chromahud;


import cc.hyperium.mods.chromahud.displayitems.Hyperium.chromahud.*;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.mods.chromahud.api.ChromaHUDDescription;
import cc.hyperium.mods.chromahud.api.ChromaHUDParser;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.chromahud.displayitems.Hyperium.chromahud.*;

import java.util.HashMap;
import java.util.Map;

public class DefaultChromaHUDParser implements ChromaHUDParser {
    private HashMap<String, String> names = new HashMap<>();

    public DefaultChromaHUDParser() {
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
            case "CORDS":
                return new CordsDisplay(item, ord);
            case "TEXT":
                return new TextItem(item, ord);
            case "ARMOUR_HUD":
                return new ArmourHud(item, ord);
            case "POTION":
                return new PotionEffects(item, ord);
            case "FPS":
                return new FPS(item, ord);
            case "PING":
                return new PingDisplay(item, ord);
            case "DIRECTION":
                return new DirectionHUD(item, ord);
            case "CPS":
                return new CpsDisplay(item, ord);
            case "ARROW_COUNT":
                return new ArrowCount(item, ord);
            case "C_COUNTER":
                return new CCounter(item, ord);
            case "TIME":
                return new TimeHud(item, ord);
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
