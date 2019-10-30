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

package cc.hyperium.utils.mods;

import cc.hyperium.config.Settings;
import cc.hyperium.event.network.chat.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;

public class CompactChat {

    // Create an instance to be used in other classes
    private static CompactChat instance;

    // Catch the last message sent
    private String lastMessage = "";

    // What line was the message on?
    private int line;

    // How many times has it been sent?
    private int amount;

    /**
     * Create the instance
     *
     * @return the class instance
     */
    public static CompactChat getInstance() {
        if (instance == null) instance = new CompactChat();
        return instance;
    }

    /**
     * Check for when a message comes through
     *
     * @param event called whenever a message is sent through chat
     */
    @InvokeEvent(priority = Priority.LOW)
    public void onChat(ChatEvent event) {
        // Check for the if the user has it enabled and if the message wasn't cancelled
        if (Settings.COMPACT_CHAT && !event.isCancelled()) {

            // Get the chat instance
            GuiNewChat guiNewChat = Minecraft.getMinecraft().ingameGUI.getChatGUI();

            // If the last message sent is the same as the newly posted message
            if (lastMessage.equals(event.getChat().getUnformattedText())) {

                // Delete the message
                guiNewChat.deleteChatLine(line);

                // Increase the amount of times it's been sent
                amount++;

                // Set the last message to be the newly posted message
                lastMessage = event.getChat().getUnformattedText();

                // Append (amount of times it's been sent) to the last message
                event.getChat().appendText(ChatColor.GRAY + " (" + amount + ")");
            } else {

                // Otherwise it's never been sent
                amount = 1;

                // Set the last message to be the newly posted message
                lastMessage = event.getChat().getUnformattedText();
            }

            // Increase the line the message was on
            line++;

            // Check if the event wasn't cancelled
            if (!event.isCancelled()) {

                // Print the chat message and allow it to be deleted
                guiNewChat.printChatMessageWithOptionalDeletion(event.getChat(), line);
            }

            // If the message has been sent 256 times
            if (line > 256) {

                // Set it to 0 again
                line = 0;
            }

            // Cancel the message
            event.setCancelled(true);
        }
    }
}
