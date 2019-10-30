package club.sk1er.website.api.requests;

import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.utils.WebsiteUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mitchellkatz on 6/4/17.
 */
public class HypixelApiGuild implements HypixelApiObject {
    private JsonHolder guild;
    private List<GuildRank> ranks;

    public HypixelApiGuild(JsonHolder master) {

        guild = master == null ? new JsonHolder() : master;
    }

    @Override
    public JsonHolder getData() {
        return guild;
    }

    public boolean isValid() {
        return guild.has("guild");
    }

    public JsonHolder getRoot() {
        return guild.optJSONObject("guild");
    }

    public String getName() {
        return getRoot().optString("name");
    }

    public boolean isLoaded() {
        return guild.optBoolean("loaded");
    }

    public String getID() {
        return getRoot().optString("_id");
    }

    public String getFormatedTag() {
        return !getTag().isEmpty() ? (getRoot().has("tagColor") ?
            WebsiteUtils.getColor(getRoot().optString("tagColor")) : "§7") + "[" + getTag() + "]" : "";
    }

    public String getTag() {
        return getRoot().optString("tag");
    }

    public int getLegacyRanking() {
        return getRoot().optInt("legacyRanking") + 1;
    }

    public String getDiscord() {
        return getRoot().optString("discord");
    }

    public boolean hasDiscord() {
        return !(getDiscord().isEmpty() || getDiscord().equalsIgnoreCase("null"));
    }

    public String getDescription() {
        return getRoot().optString("description");
    }

    public boolean canTag() {
        return getRoot().optBoolean("canTag");
    }

    public JsonArray getMembers() {
        return getRoot().optJSONArray("members");
    }

    public int getCurrentGuildCoins() {
        return getRoot().optInt("coins");
    }

    public int getTotalGuildCoins() {
        return getRoot().optInt("coinsEver");
    }

    public long getCreateDate() {
        return getRoot().optLong("created");
    }

    public boolean isJoinable() {
        return getRoot().optBoolean("joinable");
    }

    public double getLevel() {
        return getRoot().optDouble("level_calc");
    }

    public int getWins() {
        return getRoot().optJSONObject("achievements").optInt("WINNERS");
    }

    public List<String> getMembersUUID() {
        ArrayList<String> meme = new ArrayList<>();
        for (JsonElement element : getMembers()) {
            meme.add(new JsonHolder(element.getAsJsonObject()).optString("uuid"));
        }
        return meme;
    }

    public int getLevelPosition() {
        return getRoot().optInt("level_pos");
    }

    public List<GuildPlayer> getInOrder() {
        List<GuildPlayer> players = new ArrayList<>();
        for (JsonElement element : getMembers()) {
            players.add(new GuildPlayer(new JsonHolder(element.getAsJsonObject())));
        }
        players.sort(Comparator.comparingInt(o -> getPriorityForRank(o.getRank())));
        Collections.reverse(players);
        return players;
    }

    public int getWins(String item) {
        JsonHolder holder = getRoot().optJSONObject("games");
        return holder.optInt(item);
    }

    public int getPriorityForRank(String rank) {
        return getCustomRanks().stream().filter(guildRank -> guildRank.name.equals(rank)).findFirst().map(guildRank -> guildRank.priority).orElse(-1);
    }

    public List<GuildRank> getCustomRanks() {
        if (ranks == null) {
            ranks = new ArrayList<>();
            ranks.add(new GuildRank("GUILDMASTER", Integer.MAX_VALUE));
            ranks.add(new GuildRank("OFFICER", 4));
            ranks.add(new GuildRank("MEMBER", 3));

            for (JsonElement element : getRoot().optJSONArray("ranks")) {
                JsonHolder holder = new JsonHolder(element.getAsJsonObject());
                ranks.add(new GuildRank(holder.optString("name"), holder.optInt("priority")));
            }
        }
        return ranks;
    }

    public static class GuildPlayer {
        private JsonHolder object;

        public GuildPlayer(JsonHolder object) {
            this.object = object;
        }

        public String getUuid() {
            return object.optString("uuid");
        }

        public long getJoinLong() {
            return object.optLong("joined");
        }

        public String getDisplay() {
            return object.optString("displayname");
        }

        public String getRank() {
            return object.optString("rank");
        }
    }

    public static class GuildRank {
        private String name;
        private int priority;

        public GuildRank(String name, int priority) {

            this.name = name;
            this.priority = priority;
        }

        public String getName() {
            return name;
        }


        public int getPriority() {
            return priority;
        }
    }
}
