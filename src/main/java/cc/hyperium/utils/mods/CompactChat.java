/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils.mods;

import cc.hyperium.config.Settings;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;

public class CompactChat {

    private static CompactChat instance;

    private String lastMessage = "";

    private int line = 0;
    private int amount = 0;

    public static CompactChat getInstance() {
        if (instance == null) {
            instance = new CompactChat();
        }
        return instance;
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onChat(ChatEvent event) {
        if (Settings.COMPACT_CHAT && !event.isCancelled()) {
            GuiNewChat guiNewChat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
            if (this.lastMessage.equals(event.getChat().getUnformattedText())) {
                guiNewChat.deleteChatLine(line);
                this.amount++;
                this.lastMessage = event.getChat().getUnformattedText();
                event.getChat().appendText(ChatColor.GRAY + " (" + amount + ")");
            } else {
                this.amount = 1;
                this.lastMessage = event.getChat().getUnformattedText();
            }
            this.line++;
            if (!event.isCancelled()) {
                guiNewChat.printChatMessageWithOptionalDeletion(event.getChat(), line);
            }
            if (line > 256) {
                line = 0; // yeah...
            }
            event.setCancelled(true);
        }
    }

}
