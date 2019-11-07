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

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

@SuppressWarnings("unused")
public class GuildChatHandler extends HyperiumChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        String playerJoinEndStr = " joined the guild!";
        if (text.endsWith(playerJoinEndStr) && Settings.SEND_GUILD_WELCOME_MESSAGE) {
            int rankHeader = 0;
            if (text.contains("[")) rankHeader = text.indexOf("]") + 1;

            String playerName = String.valueOf(text.subSequence(rankHeader, text.length() - playerJoinEndStr.length())).trim();
            String message = "/gc Welcome to the guild " + playerName + "!";

            Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
        }

        return false;
    }
}
