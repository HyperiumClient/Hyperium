/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.remoteresources.HyperiumResource;
import cc.hyperium.handlers.handlers.remoteresources.RemoteResourcesHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.util.IChatComponent;

import java.util.regex.Pattern;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public abstract class HyperiumChatHandler {
    //Resource *should* be loaded by then sooooo
    protected static HyperiumResource regexs;
    protected static Pattern guildChatPattern = null;
    protected static Pattern partyChatPattern = null;
    protected static Pattern skywarsRankedRating = null;
    protected static Pattern privateMessageTo = null;
    protected static Pattern privateMessageFrom = null;

    static {
        Multithreading.runAsync(() -> {

            HyperiumHandlers handlers;
            while((handlers = Hyperium.INSTANCE.getHandlers()) == null) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handlers.getRemoteResourcesHandler().getResourceAsync("chat_regex", RemoteResourcesHandler.ResourceType.TEXT, HyperiumResource -> {
                regexs = HyperiumResource;
                guildChatPattern = Pattern.compile(regexs.getasJson().optString("guild_chat"));
                partyChatPattern = Pattern.compile(regexs.getasJson().optString("party_chat"));
                skywarsRankedRating = Pattern.compile(regexs.getasJson().optString("skywars_rating"));
                privateMessageTo = Pattern.compile(regexs.getasJson().optString("private_message_to"));
                privateMessageFrom = Pattern.compile(regexs.getasJson().optString("private_message_from"));
            });
        });

        ;
    }

    public Hyperium getHyperium() {
        return Hyperium.INSTANCE;
    }

    /**
     * @param component Entire component from evnet
     * @param text      Pure text for parsign
     * @return boolean to cancel event
     */
    public abstract boolean chatReceived(IChatComponent component, String text);

}
