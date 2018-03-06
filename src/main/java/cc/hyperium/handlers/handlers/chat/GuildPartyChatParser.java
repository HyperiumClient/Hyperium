/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
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
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;

/**
 * Created by mitchellkatz on 2/19/18. Designed for production use on Sk1er.club
 */
public class GuildPartyChatParser extends HyperiumChatHandler {
    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        Matcher guildMatcher = guildChatPattern.matcher(text);
        if (guildMatcher.matches()) {
            String player = guildMatcher.group("player");
            String message = guildMatcher.group("message");
            Hyperium.INSTANCE.getHandlers().getPrivateMessageHandler().getChat("guild").newMessage(message, player, Minecraft.getMinecraft().getSession().getUsername().equals(player));
        }
        Matcher partyMatcher = partyChatPattern.matcher(text);
        if (partyMatcher.matches()) {
            String player = partyMatcher.group("player");
            String message = partyMatcher.group("message");
            Hyperium.INSTANCE.getHandlers().getPrivateMessageHandler().getChat("party").newMessage(message, player, Minecraft.getMinecraft().getSession().getUsername().equals(player));
        }
        return false;
    }
}
