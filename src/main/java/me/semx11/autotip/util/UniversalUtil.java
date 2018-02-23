/*
 * Hyperium Client, Free client with huds and popular mod
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

package me.semx11.autotip.util;

import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class UniversalUtil {


    public static String getRemoteAddress(ServerJoinEvent event) {
        return event.getServer();
    }


    static void chatMessage(String text) {
        chatMessage(createComponent(text));
    }

    static void chatMessage(String text, String url, String hoverText) {
        chatMessage(createComponent(text, url, hoverText));
    }

    private static void chatMessage(Object component) {
        GeneralChatHandler.instance().sendMessage(((IChatComponent) component));
    }

    private static Object createComponent(String text) {
        return new ChatComponentText(text);
    }

    // Don't try this at home.
    private static Object createComponent(String text, String url, String hoverText) {
        IChatComponent component = new ChatComponentText(text);
        ChatStyle style = new ChatStyle();
        if (url != null) {
            style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        }
        if (hoverText != null) {
            style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverText)));
        }
        component.setChatStyle(style);
        return component;
    }

}
