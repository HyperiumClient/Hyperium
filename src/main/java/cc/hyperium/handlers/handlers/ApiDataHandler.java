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

package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import club.sk1er.website.api.requests.HypixelApiFriends;
import club.sk1er.website.api.requests.HypixelApiGuild;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ApiDataHandler {

    //Only User's data for now. Will branch out to other things soon

    private final Map<String, HypixelApiPlayer> players = new ConcurrentHashMap<>();
    private final Map<String, HypixelApiFriends> friends = new ConcurrentHashMap<>();

    private final List<UUID> friendUUIDList = new ArrayList<>();
    private final ConcurrentHashMap<String, HypixelApiGuild> guilds_player = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, HypixelApiGuild> guilds_name = new ConcurrentHashMap<>();

    public ApiDataHandler() {

    }

    public void post() {
        Multithreading.schedule(() -> {
            try {
                if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
                    refreshFriends();
                    refreshPlayer();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 10L, 305, TimeUnit.SECONDS);
    }

    public HypixelApiPlayer getPlayer(String name) {
        HypixelApiPlayer hypixelApiPlayer = players.get(name.toLowerCase());
        if (hypixelApiPlayer != null && System.currentTimeMillis() - hypixelApiPlayer.getData().optLong("local_cache") < TimeUnit.MINUTES.toMillis(5))
            return hypixelApiPlayer;

        //Expired cache
        HypixelApiPlayer tmp_player = new HypixelApiPlayer(new JsonHolder());
        tmp_player.getData().put("local_cache", System.currentTimeMillis()*2);
        if (hypixelApiPlayer == null) {
            players.put(name.toLowerCase(), tmp_player);
            Multithreading.runAsync(() -> {
                String raw = Sk1erMod.getInstance().
                        rawWithAgent("https://api.sk1er.club/player/" + name.toLowerCase());
                HypixelApiPlayer player = new HypixelApiPlayer(new JsonHolder(raw));
                player.getData().put("local_cache", System.currentTimeMillis());
                player.getData().put("loaded", true);
                if (players.size() > 1000)
                    players.clear();
                players.put(name.toLowerCase(), player);
            });
        }
        return hypixelApiPlayer == null ? new HypixelApiPlayer(new JsonHolder()) : hypixelApiPlayer;
    }

    public HypixelApiFriends getFriends(String name) {
        HypixelApiFriends hypixelApiFriends = friends.get(name.toLowerCase());
        if (hypixelApiFriends != null && System.currentTimeMillis() - hypixelApiFriends.getData().optLong("local_cache") < TimeUnit.MINUTES.toMillis(5))
            return hypixelApiFriends;

        HypixelApiFriends tmp_friends = new HypixelApiFriends(new JsonHolder());
        tmp_friends.getData().put("local_cache",  System.currentTimeMillis()*2);
        if (hypixelApiFriends == null) {
            friends.put(name.toLowerCase(), tmp_friends);
            Multithreading.runAsync(() -> {
                String raw = Sk1erMod.getInstance().
                        rawWithAgent("https://api.sk1er.club/friends/" + name.toLowerCase());
                HypixelApiFriends friends = new HypixelApiFriends(new JsonHolder(raw));
                friends.getData().put("local_cache", System.currentTimeMillis());
                friends.getData().put("loaded",true);
                if (this.friends.size() > 1000)
                    this.friends.clear();
                this.friends.put(name.toLowerCase(), friends);
            });
        }
        return hypixelApiFriends == null ? new HypixelApiFriends(new JsonHolder()) : hypixelApiFriends;
    }

    public List<UUID> getFriendUUIDList() {
        return friendUUIDList;
    }

    public void refreshFriends() {

        Multithreading.runAsync(() -> {
            try {
                getFriends(UUIDUtil.getUUIDWithoutDashes()).getData().put("local_cache", 1);
                HypixelApiFriends friends = getFriends(UUIDUtil.getUUIDWithoutDashes());
                friendUUIDList.clear();
                for (JsonElement friend : friends.getFriends()) {
                    friendUUIDList.add(cc.hyperium.netty.utils.Utils.dashMeUp(new JsonHolder(friend.getAsJsonObject()).optString("uuid")));
                }
            } catch (Exception e) {
                e.printStackTrace();
                GeneralChatHandler.instance().sendMessage("Something went wrong while loading your friends");
            }
        });
    }

    public JsonHolder getFriends() {
        //5 minute cache
        return getFriends(UUIDUtil.getUUIDWithoutDashes()).getData();
    }

    private void refreshPlayer() {
        getPlayer(UUIDUtil.getUUIDWithoutDashes()).getData().put("local_cache", 1);
    }

    public HypixelApiPlayer getPlayer() {
        return getPlayer(UUIDUtil.getUUIDWithoutDashes());
    }


    public HypixelApiGuild getGuildByPlayer(String name) {
        HypixelApiGuild hypixelApiGuild = guilds_player.get(name.toLowerCase());
        if (hypixelApiGuild != null && System.currentTimeMillis() - hypixelApiGuild.getData().optLong("local_cache") < TimeUnit.MINUTES.toMillis(5))
            return hypixelApiGuild;

        HypixelApiGuild tmp_friends = new HypixelApiGuild(new JsonHolder());
        tmp_friends.getData().put("local_cache",  System.currentTimeMillis()*2);
        if (hypixelApiGuild == null) {
            guilds_player.put(name.toLowerCase(), tmp_friends);
            Multithreading.runAsync(() -> {
                String raw = Sk1erMod.getInstance().
                        rawWithAgent("https://api.sk1er.club/guild/player/" + name.toLowerCase());
                HypixelApiGuild guild_player = new HypixelApiGuild(new JsonHolder(raw));
                guild_player.getData().put("local_cache", System.currentTimeMillis());
                guild_player.getData().put("loaded",true);
                if (guilds_player.size() > 1000)
                    guilds_player.clear();
                guilds_player.put(name.toLowerCase(), guild_player);
            });
        }
        return hypixelApiGuild == null ? new HypixelApiGuild(new JsonHolder()) : hypixelApiGuild;
    }

    public HypixelApiGuild getGuildByName(String name) {
        HypixelApiGuild hypixelApiGuild = guilds_name.get(name.toLowerCase());
        if (hypixelApiGuild != null && System.currentTimeMillis() - hypixelApiGuild.getData().optLong("local_cache") < TimeUnit.MINUTES.toMillis(5))
            return hypixelApiGuild;

        HypixelApiGuild tmp_friends = new HypixelApiGuild(new JsonHolder());
        tmp_friends.getData().put("local_cache",  System.currentTimeMillis()*2);
        if (hypixelApiGuild == null) {
            guilds_name.put(name.toLowerCase(), tmp_friends);
            Multithreading.runAsync(() -> {
                String raw = Sk1erMod.getInstance().
                        rawWithAgent("https://api.sk1er.club/guild/name/" + name.toLowerCase());
                HypixelApiGuild guild_player = new HypixelApiGuild(new JsonHolder(raw));
                guild_player.getData().put("local_cache", System.currentTimeMillis());
                guild_player.getData().put("loaded",true);
                if (guilds_name.size() > 1000)
                    guilds_name.clear();
                guilds_name.put(name.toLowerCase(), guild_player);
            });
        }
        return hypixelApiGuild == null ? new HypixelApiGuild(new JsonHolder()) : hypixelApiGuild;
    }

}
