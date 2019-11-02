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

import cc.hyperium.utils.staff.StaffSettings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.HttpUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class StaffUtils {

    private static final HashMap<UUID, StaffSettings> STAFF_CACHE = new HashMap<>();

    public static boolean isStaff(UUID uuid) {
        return STAFF_CACHE.containsKey(uuid);
    }

    public static DotColour getColor(UUID uuid) {
        return STAFF_CACHE.get(uuid).getDotColour();
    }

    private static HashMap<UUID, StaffSettings> getStaff() throws IOException {
        HashMap<UUID, StaffSettings> staff = new HashMap<>();
        String content = HttpUtil.get(new URL("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/staff.json"));
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(content).getAsJsonArray();

        int bound = array.size();
        for (int i = 0; i < bound; i++) {
            JsonObject item = array.get(i).getAsJsonObject();
            UUID uuid = UUID.fromString(item.get("uuid").getAsString());
            String colourStr = item.get("color").getAsString().toUpperCase();
            DotColour colour = colourStr.equals("CHROMA") ? new DotColour(true, ChatColor.WHITE) : new DotColour(false, ChatColor.valueOf(colourStr));
            staff.put(uuid, new StaffSettings(colour));
        }

        return staff;
    }

    public static void clearCache() throws IOException {
        STAFF_CACHE.clear();
        STAFF_CACHE.putAll(getStaff());
    }

    public static class DotColour {
        public boolean isChroma;
        public ChatColor baseColour;

        public DotColour(boolean isChroma, ChatColor baseColour) {
            this.isChroma = isChroma;
            this.baseColour = baseColour;
        }
    }
}
