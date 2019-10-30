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

import cc.hyperium.mods.chromahud.api.ChromaHUDDescription;
import cc.hyperium.mods.chromahud.api.ChromaHUDParser;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.chromahud.displayitems.chromahud.ArmourHud;
import cc.hyperium.mods.chromahud.displayitems.chromahud.ArrowCount;
import cc.hyperium.mods.chromahud.displayitems.chromahud.CCounter;
import cc.hyperium.mods.chromahud.displayitems.chromahud.CordsDisplay;
import cc.hyperium.mods.chromahud.displayitems.chromahud.CpsDisplay;
import cc.hyperium.mods.chromahud.displayitems.chromahud.DirectionHUD;
import cc.hyperium.mods.chromahud.displayitems.chromahud.FPS;
import cc.hyperium.mods.chromahud.displayitems.chromahud.PingDisplay;
import cc.hyperium.mods.chromahud.displayitems.chromahud.PotionEffects;
import cc.hyperium.mods.chromahud.displayitems.chromahud.TextItem;
import cc.hyperium.mods.chromahud.displayitems.chromahud.TimeHud;
import cc.hyperium.utils.JsonHolder;

import java.util.HashMap;
import java.util.Map;

public class DefaultChromaHUDParser implements ChromaHUDParser {
    private final HashMap<String, String> names = new HashMap<>();

    DefaultChromaHUDParser() {
        names.put("COORDS", "Coords");
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
            case "COORDS":
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
