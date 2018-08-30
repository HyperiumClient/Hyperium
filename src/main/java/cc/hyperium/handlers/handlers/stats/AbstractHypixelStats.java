package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.C;
import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.stats.display.DisplayLine;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import club.sk1er.website.utils.WebsiteUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.hypixel.api.GameType;
import net.minecraft.util.EnumChatFormatting;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public abstract class AbstractHypixelStats {


    public abstract String getImage();

    public abstract String getName();

    public abstract GameType getGameType();

    private boolean isToday(long last_completed) {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("EST"));
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeZone(TimeZone.getTimeZone("EST"));
        timeToCheck.setTimeInMillis(last_completed);
        return now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR);
    }

    public List<StatsDisplayItem> getQuests(HypixelApiPlayer player) {
        JsonHolder quests = Hyperium.INSTANCE.getHandlers().getDataHandler().getQuests();
        ArrayList<StatsDisplayItem> statsDisplayItems = new ArrayList<>();

        for (JsonElement daily : quests.optJSONArray("daily")) {
            JsonHolder obj = new JsonHolder(daily.getAsJsonObject());
            List<String> keys = obj.getKeys();
            if (keys.size() == 1) {
                String name = keys.get(0);
                if (name.equalsIgnoreCase(getGameType().name())) {
                    for (JsonElement jsonElement : obj.optJSONArray(name)) {
                        JsonHolder item = new JsonHolder(jsonElement.getAsJsonObject());
                        if (item.getKeys().size() == 1) {
                            String quest_backend = item.getKeys().get(0);
                            StringBuilder tmp = new StringBuilder(quest_backend);
                            //TODO get quest names
                            JsonHolder playerQuestData = player.getQuests().optJSONObject(quest_backend);
                            long last_completed = playerQuestData.optLong("last_completed");
                            tmp.append(": ");
                            tmp.append(isToday(last_completed) ? C.GREEN + "Completed" : C.RED + "Not Completed");
                            statsDisplayItems.add(new DisplayLine(tmp.toString()));
                            JsonArray tasks = item.optJSONArray(quest_backend);
                            JsonHolder objectives = playerQuestData.optJSONObject("active").optJSONObject("objectives");
                            for (JsonElement task : tasks) {
                                JsonHolder task1 = new JsonHolder(task.getAsJsonObject());
                                String line = task1.optString("text") + (task1.has("id") ? " (" + objectives.optInt(task1.optString("id")) + "/" + task1.optInt("goal") +")": "")+"";
                                statsDisplayItems.add(new DisplayLine("  - " + line));
                            }
                        }
                    }
                }
            }
        }
        return statsDisplayItems;
    }

    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        ArrayList<StatsDisplayItem> items = new ArrayList<>();
        GameType gameType = GameType.fromRealName(getName());
        if (gameType == null) {
            items.add(new DisplayLine("No default preview for " + getName(), Color.WHITE.getRGB()));
        } else {
            JsonHolder stats = player.getStats(gameType);
            items.add(new DisplayLine(bold("Coins: ", stats.optInt("coins")), Color.WHITE.getRGB()));
            items.add(new DisplayLine(bold("Kills: ", stats.optInt("kills")), Color.WHITE.getRGB()));
            items.add(new DisplayLine(bold("Deaths: ", stats.optInt("deaths")), Color.WHITE.getRGB()));
            items.add(new DisplayLine(bold("Wins: ", stats.optInt("wins")), Color.WHITE.getRGB()));
            items.add(new DisplayLine(bold("Losses: ", stats.optInt("losses")), Color.WHITE.getRGB()));
            items.add(new DisplayLine(bold("K/D: ", WebsiteUtils.buildRatio(stats.optInt("kills"), stats.optInt("deaths"))), Color.WHITE.getRGB()));
            items.add(new DisplayLine(bold("W/L: ", WebsiteUtils.buildRatio(stats.optInt("wins"), stats.optInt("losses"))), Color.WHITE.getRGB()));
        }
        return items;
    }

    public String bold(String bold, String rest) {
        return EnumChatFormatting.BOLD + bold + EnumChatFormatting.GRAY + rest;
    }

    public String bold(String bold, Number rest) {
        return EnumChatFormatting.BOLD + bold + EnumChatFormatting.GRAY + WebsiteUtils.comma(rest);
    }

    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        return new ArrayList<>();
    }


}
