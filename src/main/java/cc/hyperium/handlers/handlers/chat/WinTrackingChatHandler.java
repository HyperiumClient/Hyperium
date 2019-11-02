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
import cc.hyperium.event.network.server.hypixel.HypixelWinEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;

/*
 * Created by Cubxity on 21/03/2018
 */
public class WinTrackingChatHandler extends HyperiumChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        Matcher matcher = regexPatterns.get(ChatRegexType.WIN).matcher(text);
        if (matcher.matches()) {
            String winnersString = matcher.group("winners");
            String[] winners = winnersString.split(", ");

            // Means they have a rank prefix. We don't want that
            for (int i = 0; i < winners.length; i++) {
                String winner = winners[i];
                if (winner.contains(" ")) {
                    winners[i] = winner.split(" ")[1];
                }
            }

            EventBus.INSTANCE.post(new HypixelWinEvent(Arrays.asList(winners)));
        }

        // Should actually change the regex tho
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer == null) return false;

        if (text.toLowerCase().contains(thePlayer.getName().toLowerCase() + " winner!")) {
            EventBus.INSTANCE.post(new HypixelWinEvent(Collections.singletonList(thePlayer.getName())));
        }

        return false;
    }
}
