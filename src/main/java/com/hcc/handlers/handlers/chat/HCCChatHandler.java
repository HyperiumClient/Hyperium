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
import com.hcc.handlers.handlers.remoteresources.HCCResource;
import com.hcc.handlers.handlers.remoteresources.RemoteResourcesHandler;
import net.minecraft.util.IChatComponent;

import java.util.regex.Pattern;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public abstract class HCCChatHandler {
    //Resource *should* be loaded by then sooooo
    protected static final HCCResource regexs = HCC.INSTANCE.getHandlers().getRemoteResourcesHandler().getResourceSync("chat_regex", RemoteResourcesHandler.ResourceType.TEXT);
    protected static final Pattern guildChatPattern = Pattern.compile(regexs.getasJson().optString("guild_chat"));
    protected static final Pattern partyChatPattern = Pattern.compile(regexs.getasJson().optString("party_chat"));
    protected static final Pattern skywarsRankedRating = Pattern.compile(regexs.getasJson().optString("skywars_rating"));
    protected static final Pattern privateMessageTo = Pattern.compile(regexs.getasJson().optString("private_message_to"));
    protected static final Pattern privateMessageFrom = Pattern.compile(regexs.getasJson().optString("private_message_from"));


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
