/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.mods.chromahud;


import cc.hyperium.mods.chromahud.api.ChromaHUDDescription;
import cc.hyperium.mods.chromahud.api.ChromaHUDParser;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.chromahud.displayitems.hyperium.HypixelDisplay;
import cc.hyperium.mods.chromahud.displayitems.hyperium.LocationDisplay;
import cc.hyperium.mods.chromahud.displayitems.hyperium.RatingDisplay;
import cc.hyperium.mods.chromahud.displayitems.hyperium.ScoreboardDisplay;
import cc.hyperium.utils.JsonHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public class HyperiumChromaHudParser implements ChromaHUDParser {
    private HashMap<String, String> names = new HashMap<>();

    public HyperiumChromaHudParser() {
        names.put("LOCATION", "Location");
        names.put("HYPIXEL", "Hypixel");
        names.put("RATING", "Rating");
        names.put("SCOREBOARD", "Scoreboard");
    }

    @Override
    public DisplayItem parse(String type, int ord, JsonHolder item) {
        switch (type) {

            case "LOCATION":
                return new LocationDisplay(item, ord);
            case "HYPIXEL":
                return new HypixelDisplay(item, ord);
            case "RATING":
                return new RatingDisplay(item, ord);
            case "SCOREBOARD":
                return new ScoreboardDisplay(item, ord);

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
