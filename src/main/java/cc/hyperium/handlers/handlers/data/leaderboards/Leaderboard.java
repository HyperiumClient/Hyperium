package cc.hyperium.handlers.handlers.data.leaderboards;

import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Leaderboard {

    private String name;
    private String id;
    private boolean fetched = false;
    private boolean loaded = false;
    private List<LeaderboardEntry> entries = new ArrayList<>();

    public Leaderboard(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean isFetched() {
        return fetched;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public List<LeaderboardEntry> getEntries() {
        return entries;
    }

    public void ensureLoaded() {
        if (!fetched) {
            fetched = true;

            forceFetch();
        }
    }

    public void forceFetch() {

        JsonHolder leaderboard = null;
        try {
            leaderboard = HypixelAPI.INSTANCE.getLeaderboardWithID(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        List<LeaderboardEntry> data = new ArrayList<>();
        for (JsonElement element : leaderboard.optJSONArray("data")) {
            JsonHolder holder = new JsonHolder(element.getAsJsonObject());
            data.add(new LeaderboardEntry(holder.optInt("pos"), holder.optString("value"), holder.optString("uuid"), holder.optString("display")));
        }
        this.entries = data;
        loaded = true;
    }

}
