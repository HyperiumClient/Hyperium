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
