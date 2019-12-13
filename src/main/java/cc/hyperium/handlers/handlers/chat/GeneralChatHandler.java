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

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.event.network.chat.ChatEvent;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

/**
 * A util which allows the client to queue messages for the client to see when the join a game.
 * <p>
 * If the client is already playing, the message will be send instantly
 *
 * @author Sk1er
 */
public class GeneralChatHandler {

    private static GeneralChatHandler instance;
    private final List<HyperiumChatHandler> handlerList;

    // Queued messages
    private final ConcurrentLinkedQueue<IChatComponent> messages = new ConcurrentLinkedQueue<>();

    private boolean posted;

    public GeneralChatHandler(List<HyperiumChatHandler> handlerList) {
        this.handlerList = handlerList;
        instance = this;
    }

    /**
     * Queues a {@link IChatComponent} to send to the client. If the component is null,
     * an empty message will be queued instead.
     *
     * @param component the component to send.
     */
    public void sendMessage(IChatComponent component) {
        if (component == null) {
            component = new ChatComponentText("");
        }

        messages.add(component);
    }

    /**
     * Queues a message to be sent to the client. If the message is null, this will be ignored.
     *
     * @param message   the message to send to the client (can never be null)
     * @param addHeader if true, the message will show a Hyperium prefix before it
     */
    public void sendMessage(String message, boolean addHeader) {
        if (message == null) return;

        if (addHeader) {
            message = Settings.HYPERIUM_CHAT_PREFIX ? ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + message : ChatColor.WHITE + message;
        }

        sendMessage(new ChatComponentText(message));
    }

    /**
     * Queues a message to send to the client, this message will contain a Hyperium prefix.
     * If the message is null, nothing will be queued.
     *
     * @param message the message to send to the client
     */
    public void sendMessage(String message) {
        sendMessage(message, true);
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null) return;

        while (!messages.isEmpty()) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(messages.poll());
        }
    }

    @InvokeEvent
    public void chatEvent(ChatEvent event) {
        boolean state = true;
        if (!posted) return;

        for (HyperiumChatHandler chatHandler : handlerList) {
            // Surround in try catch so errors don't stop further chat parsers
            try {
                // Is reversed because chathandlers weren't called if state was false, since
                // false && boolean will always be false, so it skipped the
                // HyperiumChatHandler#chatReceived method
                state = chatHandler.chatReceived(event.getChat(), strip(event.getChat())) && state;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (state) event.setCancelled(true);
    }

    public void post() {
        JsonHolder data = new JsonHolder(new JsonParser().parse(new InputStreamReader(GeneralChatHandler.class
            .getResourceAsStream("/remoteresources/chat_regex.json"))).getAsJsonObject());

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
    }

    /**
     * Returns a completely stripped message from a {@link IChatComponent}
     *
     * @param component the component to strip
     * @return a message completely stripped of all color codes
     */
    public static String strip(IChatComponent component) {
        return ChatColor.stripColor(component.getUnformattedText());
    }

    /**
     * Returns this Chat Handlers instance, can be used anywhere.
     *
     * @return the classes instance
     */
    public static GeneralChatHandler instance() {
        return instance;
    }
}
