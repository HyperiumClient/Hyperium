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

import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public class GeneralChatHandler {
    private static GeneralChatHandler generalChatHandler = null;
    private List<HyperiumChatHandler> handlerList;

    private ConcurrentLinkedQueue<IChatComponent> messages = new ConcurrentLinkedQueue<>();

    public GeneralChatHandler(List<HyperiumChatHandler> handlerList) {
        this.handlerList = handlerList;
        generalChatHandler = this;
    }

    public static GeneralChatHandler instance() {
        return generalChatHandler;
    }

    public static String strip(IChatComponent component) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(component.getUnformattedText());
    }

    public void sendMessage(IChatComponent component) {
        messages.add(component);
    }

    public void sendMessage(String message, boolean addHeader) {
        if (addHeader) {
            message = ChatColor.RED + "[Hyperium] " + ChatColor.WHITE.toString() + message;
        }
        sendMessage(new ChatComponentText(message));
    }

    public void sendMessage(String message) {
        sendMessage(message, true);
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null)
            return;
        while (!messages.isEmpty()) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(messages.poll());
        }
    }

    @InvokeEvent
    public void chatEvent(ChatEvent event) {
        for (HyperiumChatHandler HyperiumChatHandler : handlerList) {
            //Surround in try catch so errors don't stop further chat parsers
            try {
                //not ready
                if(HyperiumChatHandler.regexs ==null) {
                    return;
                }
                //todo add canceling of event
                HyperiumChatHandler.chatReceived(event.getChat(), strip(event.getChat()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
