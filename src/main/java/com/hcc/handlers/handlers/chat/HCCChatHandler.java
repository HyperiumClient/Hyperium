/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.handlers.handlers.chat;

import com.hcc.HCC;
import com.hcc.handlers.HCCHandlers;
import com.hcc.handlers.handlers.remoteresources.HCCResource;
import com.hcc.handlers.handlers.remoteresources.RemoteResourcesHandler;
import com.hcc.mods.sk1ercommon.Multithreading;
import net.minecraft.util.IChatComponent;

import java.util.regex.Pattern;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public abstract class HCCChatHandler {
    //Resource *should* be loaded by then sooooo
    protected static HCCResource regexs;
    protected static Pattern guildChatPattern = null;
    protected static Pattern partyChatPattern = null;
    protected static Pattern skywarsRankedRating = null;
    protected static Pattern privateMessageTo = null;
    protected static Pattern privateMessageFrom = null;

    static {
        Multithreading.runAsync(() -> {

            HCCHandlers handlers;
            while((handlers = HCC.INSTANCE.getHandlers()) == null) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handlers.getRemoteResourcesHandler().getResourceAsync("chat_regex", RemoteResourcesHandler.ResourceType.TEXT, hccResource -> {
                regexs = hccResource;
                guildChatPattern = Pattern.compile(regexs.getasJson().optString("guild_chat"));
                partyChatPattern = Pattern.compile(regexs.getasJson().optString("party_chat"));
                skywarsRankedRating = Pattern.compile(regexs.getasJson().optString("skywars_rating"));
                privateMessageTo = Pattern.compile(regexs.getasJson().optString("private_message_to"));
                privateMessageFrom = Pattern.compile(regexs.getasJson().optString("private_message_from"));
            });
        });

        ;
    }

    public HCC getHcc() {
        return HCC.INSTANCE;
    }

    /**
     * @param component Entire component from evnet
     * @param text      Pure text for parsign
     * @return boolean to cancel event
     */
    public abstract boolean chatReceived(IChatComponent component, String text);

}
