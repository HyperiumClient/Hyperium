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

package cc.hyperium.handlers.handlers.data;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.data.leaderboards.Leaderboard;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ApiDataHandler {

    //Only User's data for now. Will branch out to other things soon

    private final Map<String, HypixelApiPlayer> otherPlayers = new ConcurrentHashMap<>();
    private JsonHolder friends = new JsonHolder();
    private HypixelApiPlayer player = new HypixelApiPlayer(new JsonHolder());
    private List<UUID> friendUUIDList = new ArrayList<>();
    private List<Leaderboard> leaderboards;

    public ApiDataHandler() {
        getLeaderboards();
    }

    public void post() {
        Multithreading.schedule(() -> {
            try {
                if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel())
                    refreshFriends();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 10L, 305, TimeUnit.SECONDS);
    }

    public HypixelApiPlayer getPlayer(String name) {
        return otherPlayers.computeIfAbsent(name.toLowerCase(), s -> {
            Multithreading.runAsync(() -> {
                HypixelApiPlayer player = new HypixelApiPlayer(new JsonHolder(getUrl("https://sk1er.club/data/" +
                        s +
                        "/" + Sk1erMod.getInstance().getApIKey())));
                player.getRoot().put("localCache", System.currentTimeMillis());
                if (otherPlayers.size() > 1000)
                    otherPlayers.clear();
                otherPlayers.put(name.toLowerCase(), player);

            });
            return new HypixelApiPlayer(new JsonHolder());
        });

    }

    public List<UUID> getFriendUUIDList() {
        return friendUUIDList;
    }

    public void refreshFriends() {
        if (friends.optBoolean("fetching"))
            return;
        friends.put("fetching", true);
        Multithreading.runAsync(() -> {
            try {
                friends = new JsonHolder(getUrl("https://sk1er.club/modquery/" + Sk1erMod.getInstance().getApIKey() + "/friends/" +
                        Minecraft.getMinecraft().getSession().getPlayerID()))
                        .put("localCache", System.currentTimeMillis());
                friendUUIDList = new ArrayList<>();
                for (String s : friends.getKeys()) {
                    try {
                        friendUUIDList.add(cc.hyperium.netty.utils.Utils.dashMeUp(s));
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                GeneralChatHandler.instance().sendMessage("Something went wrong while loading your friends");
            }
        });
    }

    public JsonHolder getFriends() {
        //5 minute cache
        if (System.currentTimeMillis() - friends.optLong("localCache") > 1000 * 60 * 5)
            refreshFriends();
        return friends;
    }

    private void refreshPlayer() {
        if (player.getRoot().optBoolean("fetching"))
            return;
        player.getRoot().put("fetching", true);
        Multithreading.runAsync(() -> {
            try {
                player = new HypixelApiPlayer(new JsonHolder(getUrl("https://sk1er.club/data/" +
                        Minecraft.getMinecraft().getSession().getPlayerID() +
                        "/" + Sk1erMod.getInstance().getApIKey())));
                player.getRoot().put("localCache", System.currentTimeMillis());
            } catch (Exception e) {

                GeneralChatHandler.instance().sendMessage("Something went wrong while loading your data");
            }
        });
    }

    public HypixelApiPlayer getPlayer() {
        if (System.currentTimeMillis() - player.getRoot().optLong("localCache") > 1000 * 60 * 5)
            refreshPlayer();
        return player;
    }

    public JsonHolder getLeaderboard(String id) {
        return new JsonHolder(getUrl("https://api.sk1er.club/leaderboard/" + id));
    }

    public List<Leaderboard> getLeaderboards() {
        if (leaderboards == null) {
            leaderboards = new ArrayList<>();
            Multithreading.runAsync(() -> {
                JsonHolder holder = new JsonHolder(getUrl("https://api.sk1er.club/leaderboards"));
                List<Leaderboard> data = new ArrayList<>();
                for (String s : holder.getKeys()) {
                    data.add(new Leaderboard(s, holder.optString(s)));
                }
                this.leaderboards = data;
            });
        }
        return leaderboards;

    }

    private String getUrl(String url) {
        url = url.replace(" ", "%20");
        //System.out.println("Fetching " + url);
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 Hyperium " + Metadata.getVersion() + "-" + Metadata.getVersionID());
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            return IOUtils.toString(is, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        object.addProperty("cause", "Exception");
        return object.toString();

    }
}
