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

package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.Hyperium;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.handlers.handlers.remoteresources.RemoteResourcesHandler;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

/**
 * @author Sk1er
 */
public class GeneralChatHandler {

    private static GeneralChatHandler generalChatHandler = null;
    private final List<HyperiumChatHandler> handlerList;

    private final ConcurrentLinkedQueue<IChatComponent> messages = new ConcurrentLinkedQueue<>();
    private boolean posted = false;

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
        boolean state = true;
        if (!posted)
            return;
        for (HyperiumChatHandler chatHandler : handlerList) {
            //Surround in try catch so errors don't stop further chat parsers
            try {
                //not ready
                if (cc.hyperium.handlers.handlers.chat.HyperiumChatHandler.regexs == null) {
                    return;
                }

                // Is reversed because chathandlers weren't called if state was false, since
                // false && boolean will always be false, so it skipped the
                // HyperiumChatHandler#chatReceived method
                state = chatHandler.chatReceived(event.getChat(), strip(event.getChat())) && state;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        event.setCancelled(state);
    }

    public void post() {
        Hyperium.INSTANCE.getHandlers().getRemoteResourcesHandler().getResourceAsync("chat_regex.json", RemoteResourcesHandler.ResourceType.TEXT, res -> {
            HyperiumChatHandler.regexs = res;
            JsonHolder data = res.getasJson();

            HyperiumChatHandler.regexPatterns = new EnumMap<>(HyperiumChatHandler.ChatRegexType.class);
            for (HyperiumChatHandler.ChatRegexType type : HyperiumChatHandler.ChatRegexType.values()) {
                if (!data.has(type.name().toLowerCase())) {
                    Hyperium.LOGGER.error("Could not find chat regex type " + type.name().toLowerCase() + " in the remote file.");
                    continue;
                }

                HyperiumChatHandler.regexPatterns.put(type, Pattern.compile(data.optString(type.name().toLowerCase())));
            }

            posted = true;
            for (HyperiumChatHandler chatHandler : handlerList) {
                chatHandler.callback(data);
            }
        });
    }
}
