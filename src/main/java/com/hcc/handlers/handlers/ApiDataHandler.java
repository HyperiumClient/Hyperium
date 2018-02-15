package com.hcc.handlers.handlers;

import com.hcc.handlers.handlers.chat.GeneralChatHandler;
import com.hcc.mods.sk1ercommon.Multithreading;
import com.hcc.mods.sk1ercommon.Sk1erMod;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;

public class ApiDataHandler {

    //Only User's data for now. Will branch out to other things soon

    private JsonHolder friends = new JsonHolder();

    public ApiDataHandler() {

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
                        .put("cache", System.currentTimeMillis());
            } catch (Exception e) {
                GeneralChatHandler.instance().sendMessage("Something went wrong while loading your friends");
            }
        });
    }

    public JsonHolder getFriends() {
        //5 minute cache
        if (System.currentTimeMillis() - friends.optLong("cache") > 1000 * 60 * 5)
            refreshFriends();
        return friends;
    }
}
