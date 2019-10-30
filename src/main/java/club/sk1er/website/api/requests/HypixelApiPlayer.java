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

package club.sk1er.website.api.requests;

import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.utils.WebsiteUtils;
import com.google.gson.JsonArray;
import net.hypixel.api.GameType;
import net.hypixel.api.util.ILeveling;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * @author Sk1er
 */
public class HypixelApiPlayer implements HypixelApiObject {

    public static final DateFormat DMY = new SimpleDateFormat("dd/MM, YYYY");
    public static final DateFormat DMYHHMMSS = new SimpleDateFormat("dd/MM, YYYY HH:mm:ss");

    private final JsonHolder player;

    public HypixelApiPlayer(JsonHolder holder) {
        player = holder;
    }

    public double getsafTotalXP() {
        return ILeveling.getTotalExpToLevel(getRoot().optInt("oldLevel") + 1) + getRoot().optInt(ILeveling.EXP_FIELD);
    }

    public JsonHolder getQuests() {
        return getRoot().optJSONObject("quests");
    }

    public int getKarma() {
        return getRoot().optInt("karma");
    }

    public long getFirstLoginLong() {
        return getRoot().optLong("firstLogin");
    }

    public long getLatestLoginLong() {
        return getRoot().optLong("lastLogin");
    }

    public String getLastLoginDate() {
        return DMY.format(new Date(getLatestLoginLong()));
    }

    public String getFirstLoginDate() {
        return DMY.format(new Date(getFirstLoginLong()));
    }

    public int getNetworkLevel() {
        return getRoot().optInt("networkLevel") + 1;
    }

    public boolean isValid() {
        return player != null && !player.isNull("player") && player.has("player");
    }

    @Override
    public JsonHolder getData() {
        return player;
    }

    public JsonArray getAliases() {
        return getRoot().optJSONArray("knownAliases");
    }

    public String getUUID() {
        return getRoot().optString("uuid");
    }

    public JsonHolder getRoot() {
        return player.optJSONObject("player");
    }

    public JsonHolder getStats() {
        return getRoot().optJSONObject("stats");
    }

    public JsonHolder getStats(GameType type) {
        return getStats().optJSONObject(type.getDbName());
    }

    public String getName() {
        return getRoot().optString("displayname");
    }

    public JsonHolder getGiftMeta() {
        return getRoot().optJSONObject("giftingMeta");
    }

    public JsonHolder getVotingMeta() {
        return getRoot().optJSONObject("voting");
    }


    public int getAchievementPoints() {
        return getRoot().optInt("points");
    }

    public boolean isInGuild() {
        return getGuild().isValid();
    }

    public HypixelApiGuild getGuild() {
        try {
            return HypixelAPI.INSTANCE.getGuildFromPlayer(getUUID()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getTotalCoins() {
        return getRoot().optInt("coins");
    }

    public int getTotalQuests() {
        return getRoot().optInt("questNumber");
    }

    public int getTotalKills() {
        return getRoot().optInt("kills");
    }

    public int getTotalWins() {
        return getRoot().optInt("wins");
    }

    public boolean has(String val) {
        return getRoot().has(val);
    }

    public GameType mostRecentGame() {
        return GameType.parse(getRoot().optString("mostRecentGameType"));
    }

    public boolean isYouTuber() {
        return getRoot().optString("rank").equalsIgnoreCase("youtuber");
    }

    public boolean isStaffOrYT() {
        return isStaff() || isYouTuber();
    }

    public boolean isStaff() {
        String rank = getRoot().optString("rank");
        return rank.equalsIgnoreCase("admin") || rank.equalsIgnoreCase("moderator") || rank.equalsIgnoreCase("helper");
    }

    public long getLastLoginLong() {
        return getRoot().optLong("lastLogin");
    }

    public long getLastLogoffLong() {
        return getRoot().optLong("lastLogout", System.currentTimeMillis());
    }

    public long getCacheTime() {
        return player.optLong("cache");
    }

    public boolean isInvalidPlayer() {
        return player.optString("cause").equalsIgnoreCase("non-player");
    }

    public String getRankForMod() {
        if (isStaff() || isYouTuber()) {
            String string = getRoot().optString("rank");
            if (!string.equalsIgnoreCase("normal")) {
                return string;
            }
        } else if (getRoot().has("newPackageRank")) {
            return getRoot().optString("newPackageRank");
        } else if (getRoot().has("packageRank")) {
            return getRoot().optString("packageRank");
        }

        return "NONE";
    }


    public String getDisplayString() {
        return getRoot().optString("display");
    }

    public Rank getRank() {
        return Rank.get(getRankForMod().toUpperCase());
    }

    public int getFriendCount() {
        return getRoot().optInt("friends");
    }

    public long getInt(String path) {
        return WebsiteUtils.get(getRoot().getObject(), path);
    }

    enum Rank {
        ADMIN,
        MODERATOR,
        HELPER,
        YOUTUBER,
        MVP_PLUS_PLUS,
        MVP_PLUS,
        MVP,
        VIP_PLUS,
        VIP,
        NONE;

        static Rank get(String in) {
            return Arrays.stream(values()).filter(rank -> rank.name().equalsIgnoreCase(in)).findFirst().orElse(NONE);
        }

        boolean has(Rank other) {
            return ordinal() >= other.ordinal();
        }
    }
}

