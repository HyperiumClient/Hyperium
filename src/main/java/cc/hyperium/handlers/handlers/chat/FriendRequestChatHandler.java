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
import cc.hyperium.event.network.server.hypixel.FriendRemoveEvent;
import cc.hyperium.event.network.server.hypixel.HypixelFriendRequestEvent;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FriendRequestChatHandler extends HyperiumChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        Matcher matcher1 = Pattern.compile("You removed ((?<rank>\\[.+] )?(?<player>\\w+)) from your friends list!").matcher(text);
        if (matcher1.find()) {
            String rank = "";

            try {
                rank = matcher1.group("rank");
            } catch (Exception ignored) {
            }

            String player = matcher1.group("player");
            EventBus.INSTANCE.post(new FriendRemoveEvent(rank + player, player));
        }

        if (!text.toLowerCase().contains("friend request")) {
            return false;
        }

        Matcher matcher = regexPatterns.get(ChatRegexType.FRIEND_REQUEST).matcher(text);

        if (matcher.find()) {
            EventBus.INSTANCE.post(new HypixelFriendRequestEvent(matcher.group("player")));
        }

        return false;
    }
}
