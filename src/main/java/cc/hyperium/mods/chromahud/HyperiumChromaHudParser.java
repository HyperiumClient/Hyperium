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

package cc.hyperium.mods.chromahud;


import cc.hyperium.mods.chromahud.api.ChromaHUDDescription;
import cc.hyperium.mods.chromahud.api.ChromaHUDParser;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.chromahud.displayitems.hyperium.*;
import cc.hyperium.utils.JsonHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sk1er
 */
public class HyperiumChromaHudParser implements ChromaHUDParser {
    private final Map<String, String> names = new HashMap<>();

    public HyperiumChromaHudParser() {
        names.put("LOCATION", "Location");
        names.put("HYPIXEL", "Hypixel");
        names.put("MINIGAME_DISPLAY", "Hypixel Minigame Display");
        names.put("RATING", "Rating");
        names.put("SCOREBOARD", "Scoreboard");
        names.put("INFO", "Hyperium Info");
        names.put("COINS", "Coin Display");
        names.put("CBCPS", "Cheatbreaker CPS");
        names.put("CBFPS", "Cheatbreaker FPS");
        names.put("DCOUNT", "Dabs Counter");
        names.put("PLAYER", "Player Display");
        names.put("BEDWARS", "BedWars Resources Display");
        names.put("DOUBLE_CPS_DISPLAY", "L+R CPS Display");
    }

    @Override
    public DisplayItem parse(String type, int ord, JsonHolder item) {
        switch (type) {
            case "INFO":
                return new HyperiumInfoDisplay(item, ord);
            case "LOCATION":
                return new LocationDisplay(item, ord);
            case "HYPIXEL":
                return new HypixelDisplay(item, ord);
            case "MINIGAME_DISPLAY":
                return new MinigameDisplay(item, ord);
            case "RATING":
                return new RatingDisplay(item, ord);
            case "COINS":
                return new CoinsDisplay(item, ord);
            case "SCOREBOARD":
                return new ScoreboardDisplay(item, ord);
            case "CBCPS":
                return new CBCpsDisplay(item, ord);
            case "CBFPS":
                return new CBFpsDisplay(item, ord);
            case "DCOUNT":
                return new DabCounter(item, ord);
            case "PLAYER":
                return new PlayerDisplay(item, ord);
            case "BEDWARS":
                return new BedwarsResourcesDisplay(item, ord);
            case "DOUBLE_CPS_DISPLAY":
                return new DoubleCPSDisplay(item, ord);
        }
        return null;
    }

    @Override
    public Map<String, String> getNames() {
        return names;
    }

    @Override
    public ChromaHUDDescription description() {
        return new ChromaHUDDescription("DEFAULT", "1.0", "Hyperium", "Default Items in Hyperium.");
    }
}
