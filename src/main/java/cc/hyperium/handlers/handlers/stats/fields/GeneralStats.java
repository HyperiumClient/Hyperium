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

package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.DisplayLine;
import cc.hyperium.handlers.handlers.stats.display.DisplayTable;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.Pet;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import net.hypixel.api.GameType;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GeneralStats extends AbstractHypixelStats {
    @Override
    public GameType getGameType() {
        return GameType.UNKNOWN;
    }

    @Override
    public String getImage() {
        return "General";
    }

    @Override
    public String getName() {
        return "General";
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        ArrayList<StatsDisplayItem> items = new ArrayList<>();
        items.add(new DisplayLine(bold("Level: ", player.getNetworkLevel()), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Karma: ", player.getKarma()), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Friends: ", player.getFriendCount()), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Achievement Points: ", player.getAchievementPoints()), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Quest Completed: ", player.getTotalQuests()), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Current Coins: ", player.getTotalCoins()), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Total Kills: ", player.getTotalKills()), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Total Wins: ", player.getTotalWins()), Color.WHITE.getRGB()));

        return items;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> items = getPreview(player);
        JsonHolder giftm = player.getGiftMeta();
        items.add(new DisplayLine(""));
        items.add(new DisplayLine(bold("Gift given: ", giftm.optInt("realBundlesGiven"))));
        items.add(new DisplayLine(bold("Gift received: ", giftm.optInt("realBundlesReceived"))));
        items.add(new DisplayLine(""));
        if (player.has("mostRecentGameType")) {
            items.add(new DisplayLine(bold("Most recent played: ", player.mostRecentGame().getName())));
            items.add(new DisplayLine(""));
        }
        items.add(new DisplayLine(bold("Rewards Claimed: ", player.getInt("totalRewards"))));
        items.add(new DisplayLine(bold("Daily Rewards Claimed: ", player.getInt("totalDailyRewards"))));
        items.add(new DisplayLine(bold("Best Rewards Streak: ", player.getInt("rewardHighScore"))));
        items.add(new DisplayLine(bold("Current reward streak: ", player.getInt("rewardScore"))));
        items.add(new DisplayLine(""));
        items.add(new DisplayLine(bold("Times voted: ", player.getInt("voting#total"))));

        items.add(new DisplayLine(""));

        JsonHolder petss2 = player.getRoot().optJSONObject("petStats");
        List<String[]> strings = new ArrayList<>();
        strings.add(new String[]{"Name", "Level"});
        for (String in : petss2.getKeys()) {
            JsonHolder tmp = petss2.optJSONObject(in);
            Pet pet = new Pet(tmp);
            String name;
            if (tmp.has("name"))
                name = tmp.optString("name");
            else name = in.replace("_", " ").toLowerCase();
            strings.add(new String[]{name, Integer.toString(pet.level)});
        }
        items.add(new DisplayLine("Pets", Color.WHITE.getRGB(), 2));
        items.add(new DisplayTable(strings));

        return items;
    }
}

