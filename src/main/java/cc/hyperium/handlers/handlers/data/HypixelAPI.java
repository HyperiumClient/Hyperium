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

package cc.hyperium.handlers.handlers.data;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.hypixel.JoinHypixelEvent;
import cc.hyperium.handlers.handlers.data.leaderboards.Leaderboard;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.netty.utils.Utils;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import club.sk1er.website.api.requests.HypixelApiFriends;
import club.sk1er.website.api.requests.HypixelApiGuild;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HypixelAPI {
    public static HypixelAPI INSTANCE;

    private final AsyncLoadingCache<String, HypixelApiPlayer> PLAYERS = Caffeine.newBuilder()
        .maximumSize(1_000)
        .expireAfterWrite(Duration.ofMinutes(5))
        .executor(Multithreading.POOL)
        .buildAsync(this::getApiPlayer);

    private final AsyncLoadingCache<String, HypixelApiFriends> FRIENDS = Caffeine.newBuilder()
        .maximumSize(1_000)
        .expireAfterWrite(Duration.ofMinutes(5))
        .executor(Multithreading.POOL)
        .buildAsync(this::getApiFriends);

    private final AsyncLoadingCache<String, HypixelApiGuild> GUILDS = Caffeine.newBuilder()
        .maximumSize(1_000)
        .expireAfterWrite(Duration.ofMinutes(5))
        .executor(Multithreading.POOL)
        .buildAsync(this::getApiGuild);

    private List<Leaderboard> LEADERBOARDS;
    private JsonHolder QUESTS;

    private List<UUID> friendsForCurrentUser = new ArrayList<>();

    public HypixelAPI() {
        Multithreading.schedule(this::updatePersonalData, 10L, 305, TimeUnit.SECONDS);
        INSTANCE = this;
        Multithreading.runAsync(() -> getQuests(true));

    }

    @InvokeEvent
    public void joinHypixel(JoinHypixelEvent event) {
        refreshCurrentUser();
        refreshFriendsForCurrentUser();
        getQuests();
    }

    /* PLAYER */

    public CompletableFuture<HypixelApiPlayer> getPlayer(String key) {
        return PLAYERS.get(key);
    }

    public CompletableFuture<HypixelApiPlayer> getCurrentUser() {
        return getPlayer(UUIDUtil.getUUIDWithoutDashes());
    }

    public void refreshPlayer(String key) {
        PLAYERS.synchronous().refresh(key);
    }

    public void refreshCurrentUser() {
        refreshPlayer(getKeyForCurrentUser());
    }

    /* FRIENDS */

    public CompletableFuture<HypixelApiFriends> getFriends(String key) {
        return FRIENDS.get(key);
    }

    public CompletableFuture<HypixelApiFriends> getFriendsForCurrentUser() {
        return getFriends(getKeyForCurrentUser()).whenComplete((data, error) -> {
            if (error != null) return;

            friendsForCurrentUser.clear();

            data.getFriends().forEach(
                friend -> friendsForCurrentUser.add(
                    Utils.dashMeUp(new JsonHolder(friend.getAsJsonObject()).optString("uuid"))
                )
            );
        });
    }

    public void refreshFriends(String key) {
        FRIENDS.synchronous().refresh(key);
    }

    public void refreshFriendsForCurrentUser() {
        refreshFriends(getKeyForCurrentUser());
    }

    public List<UUID> getListOfCurrentUsersFriends() {
        return friendsForCurrentUser;
    }

    /* LEADERBOARDS */

    public CompletableFuture<List<Leaderboard>> getLeaderboards(boolean refresh) {
        if (LEADERBOARDS != null && !refresh) {
            return CompletableFuture.completedFuture(LEADERBOARDS);
        }

        return CompletableFuture.supplyAsync(() -> {
            JsonHolder holder = new JsonHolder(
                Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/leaderboards")
            );

            return holder.getKeys().stream().map(
                key -> new Leaderboard(key, holder.optString(key))
            ).collect(Collectors.toList());
        }, Multithreading.POOL).whenComplete((leaderboards, error) -> {
            if (error != null) LEADERBOARDS = leaderboards;
        });
    }

    public CompletableFuture<List<Leaderboard>> getLeaderboards() {
        return getLeaderboards(false);
    }

    public CompletableFuture<JsonHolder> getLeaderboardWithID(String ID) {
        return CompletableFuture.supplyAsync(() -> new JsonHolder(
            Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/leaderboard/" + ID)
        ), Multithreading.POOL);
    }

    /* QUESTS */

    public CompletableFuture<JsonHolder> getQuests(boolean refresh) {
        if (QUESTS != null && !refresh) {
            return CompletableFuture.completedFuture(QUESTS);
        }

        return CompletableFuture.supplyAsync(
            () -> new JsonHolder(Sk1erMod.getInstance().rawWithAgent("https://api.hyperium.cc/quests")),
            Multithreading.POOL
        ).whenComplete((quests, error) -> {
            if (error != null) return;

            QUESTS = quests;
        });
    }

    public CompletableFuture<JsonHolder> getQuests() {
        return getQuests(false);
    }

    public String getFrontendNameOfQuest(String backendName) {
        JsonHolder quests = QUESTS.optJSONObject("quests");

        List<JsonArray> arrays = quests.getKeys().stream().map(quests::optJSONArray).collect(Collectors.toList());

        for (JsonArray array : arrays) {
            for (JsonElement element : array) {
                JsonHolder holder = new JsonHolder(element.getAsJsonObject());
                if (holder.optString("id").equalsIgnoreCase(backendName)) return holder.optString("name");
            }
        }

        return backendName;
    }

    /* GUILDS */

    public CompletableFuture<HypixelApiGuild> getGuildFromName(String name) {
        return getGuild(GuildKey.fromName(name));
    }

    public CompletableFuture<HypixelApiGuild> getGuildFromPlayer(String playerName) {
        return getGuild(GuildKey.fromPlayer(playerName));
    }

    public CompletableFuture<HypixelApiGuild> getGuild(GuildKey key) {
        return GUILDS.get(key.toString());
    }

    /* UTILS */

    private void updatePersonalData() {
        if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            refreshFriendsForCurrentUser();
            refreshCurrentUser();
        }
    }

    private String getKeyForCurrentUser() {
        return UUIDUtil.getUUIDWithoutDashes();
    }

    private HypixelApiFriends getApiFriends(String key) {
        return new HypixelApiFriends(new JsonHolder(
            Sk1erMod.getInstance().rawWithAgent(
                "https://api.sk1er.club/friends/"
                    + key.toLowerCase()
            )
        ));
    }

    private HypixelApiPlayer getApiPlayer(String key) {
        return new HypixelApiPlayer(new JsonHolder(
            Sk1erMod.getInstance().rawWithAgent(
                "https://api.sk1er.club/player/"
                    + key.toLowerCase()
            )
        ));
    }

    private HypixelApiGuild getApiGuild(String key) {
        GuildKey guildKey = GuildKey.fromSerialized(key);

        return new HypixelApiGuild(new JsonHolder(
            Sk1erMod.getInstance().rawWithAgent(
                String.format(guildKey.type.getUrl(), (Object) guildKey.formatStrings)
            )
        ));
    }

    enum GuildKeyType {
        PLAYER("https://api.sk1er.club/guild/player/%s"),
        NAME("https://api.sk1er.club/guild/name/");

        private String url;

        GuildKeyType(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class GuildKey {
        private GuildKeyType type;
        private String[] formatStrings;

        public GuildKey(GuildKeyType type, String... formatStrings) {
            this.type = type;
            this.formatStrings = formatStrings;
        }

        public static GuildKey fromSerialized(String serialized) {
            String type = serialized.split(";")[0];
            return new GuildKey(
                GuildKeyType.valueOf(type),
                serialized.split(";")[1].split(",")
            );
        }

        public static GuildKey fromName(String name) {
            return new GuildKey(GuildKeyType.NAME, name);
        }

        public static GuildKey fromPlayer(String playerName) {
            return new GuildKey(GuildKeyType.PLAYER, playerName);
        }

        @Override
        public String toString() {
            return type.toString() + ";" + String.join(",", formatStrings);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (obj instanceof GuildKey) {
                GuildKey key = ((GuildKey) obj);

                return key.type == type && Arrays.equals(key.formatStrings, formatStrings);
            }

            return false;
        }

    }
}
