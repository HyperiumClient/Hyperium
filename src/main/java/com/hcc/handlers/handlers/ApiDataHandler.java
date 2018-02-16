package com.hcc.handlers.handlers;

import club.sk1er.website.api.requests.HypixelApiPlayer;
import com.hcc.handlers.handlers.chat.GeneralChatHandler;
import com.hcc.mods.sk1ercommon.Multithreading;
import com.hcc.mods.sk1ercommon.Sk1erMod;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;

import java.util.concurrent.ConcurrentHashMap;

public class ApiDataHandler {

    //Only User's data for now. Will branch out to other things soon

    private JsonHolder friends = new JsonHolder();
    private HypixelApiPlayer player = new HypixelApiPlayer(new JsonHolder());
    private ConcurrentHashMap<String, HypixelApiPlayer> otherPlayers = new ConcurrentHashMap<>();

    public ApiDataHandler() {

    }

    public HypixelApiPlayer getPlayer(String name) {
        return otherPlayers.computeIfAbsent(name.toLowerCase(), s -> {
            Multithreading.runAsync(() -> {
                HypixelApiPlayer player = new HypixelApiPlayer(new JsonHolder(Sk1erMod.getInstance().
                        rawWithAgent("https://sk1er.club/data/" +
                                s +
                                "/" + Sk1erMod.getInstance().getApIKey())));
                player.getRoot().put("localCache", System.currentTimeMillis());
                otherPlayers.put(name.toLowerCase(), player);
            });
            return new HypixelApiPlayer(new JsonHolder());
        });
    }

    public void refreshFriends() {
        if (friends.optBoolean("fetching"))
            return;
        friends.put("fetching", true);
        Multithreading.runAsync(() -> {
            try {
                friends = new JsonHolder(Sk1erMod.getInstance().
                        rawWithAgent("https://sk1er.club/modquery/" + Sk1erMod.getInstance().getApIKey() + "/friends/" +
                                Minecraft.getMinecraft().getSession().getPlayerID()))
                        .put("localCache", System.currentTimeMillis());
            } catch (Exception e) {
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


    public void refreshPlayer() {
        if (player.getRoot().optBoolean("fetching"))
            return;
        player.getRoot().put("fetching", true);
        Multithreading.runAsync(() -> {
            try {
                player = new HypixelApiPlayer(new JsonHolder(Sk1erMod.getInstance().
                        rawWithAgent("https://sk1er.club/data/" +
                                Minecraft.getMinecraft().getSession().getPlayerID() +
                                "/" + Sk1erMod.getInstance().getApIKey())));
                player.getRoot().put("localCache", System.currentTimeMillis());
            } catch (Exception e) {
                GeneralChatHandler.instance().sendMessage("Something went wrong while loading your friends");
            }
        });
    }

    public HypixelApiPlayer getPlayer() {
        if (System.currentTimeMillis() - player.getRoot().optLong("localCache") > 1000 * 60 * 5)
            refreshPlayer();
        return player;
    }
}
