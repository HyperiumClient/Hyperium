package club.sk1er.website.api.requests;

import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.utils.WebsiteUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 6/4/17.
 */
public class HypixelApiGuild implements HypixelApiObject {
    private JsonHolder guild;

    public HypixelApiGuild(JsonHolder master) {

        this.guild = master == null ? new JsonHolder() : master;
    }

    @Override
    public JsonHolder getData() {
        return guild;
    }

    public boolean isValid() {
        return !guild.isNull("guild");
    }

    public JsonHolder getRoot() {
        return guild.optJSONObject("guild");
    }


    public String getName() {
        return getRoot().optString("name");
    }

    public boolean isLoaded() {
        return !guild.optBoolean("unloaded");
    }

    public String getID() {
        return getRoot().optString("_id");
    }

    public String getFormatedTag() {
        if (!getTag().isEmpty())
            return (getRoot().has("tagColor") ? WebsiteUtils.getColor(getRoot().optString("tagColor")) : "§7") + "[" + getTag() + "]";
        return "";
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

    public class GuildPlayer {
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
    }
}