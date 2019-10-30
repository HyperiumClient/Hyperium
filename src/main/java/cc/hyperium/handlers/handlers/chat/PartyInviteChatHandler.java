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

package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.network.server.hypixel.HypixelPartyInviteEvent;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;

public class PartyInviteChatHandler extends HyperiumChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        if (!text.toLowerCase().contains("their party!")) return false;
        Matcher matcher = regexPatterns.get(ChatRegexType.PARTY_INVITE).matcher(text);

        if (matcher.find()) {
            EventBus.INSTANCE.post(new HypixelPartyInviteEvent(matcher.group("player")));
        }

        return false;
    }
}
