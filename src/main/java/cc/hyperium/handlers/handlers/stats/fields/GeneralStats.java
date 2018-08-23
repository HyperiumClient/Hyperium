package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.DisplayLine;
import cc.hyperium.handlers.handlers.stats.display.DisplayTable;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import club.sk1er.website.api.requests.HypixelApiPlayer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GeneralStats extends AbstractHypixelStats {
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
        ArrayList<StatsDisplayItem> statsDisplayItems = new ArrayList<>();
        statsDisplayItems.add(new DisplayTable(new String[]{"Hello", "World", "Sk1er"}, new String[]{"Sk1er is the", "Best", "Quester"}));
        return statsDisplayItems;
    }
}
