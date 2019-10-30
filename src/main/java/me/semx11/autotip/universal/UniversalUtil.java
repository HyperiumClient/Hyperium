package me.semx11.autotip.universal;

import cc.hyperium.event.network.chat.ServerChatEvent;
import cc.hyperium.event.network.server.ServerJoinEvent;
import cc.hyperium.utils.ChatColor;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.util.MinecraftVersion;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class UniversalUtil {

    private static Autotip autotip;

    public static void setAutotip(Autotip autotip) {
        UniversalUtil.autotip = autotip;
    }

    public static MinecraftVersion getMinecraftVersion() {

        return MinecraftVersion.V1_8_9;
    }

    public static String getRemoteAddress(ServerJoinEvent event) {
        return event.getServer() + ":" + event.getPort();
    }

    public static String getUnformattedText(ServerChatEvent event) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(event.getChat().getUnformattedText());
    }

    public static String getUnformattedText(Object component) {
        if (component == null) {
            return null;
        }

        String unformattedText = ((IChatComponent) component).getUnformattedText();
        return ChatColor.stripColor(unformattedText);
    }

    public static String getHoverText(ServerChatEvent event) {
        return getHoverText(event.getChat());
    }

    public static String getHoverText(Object component) {
        IChatComponent chatComponent = (IChatComponent) component;

        if (component == null || chatComponent.getChatStyle() == null || chatComponent.getChatStyle().getChatHoverEvent() == null) {
            return null;
        }

        IChatComponent hoverText = chatComponent.getChatStyle().getChatHoverEvent().getValue();

        return getUnformattedText(hoverText);
    }

    public static void addChatMessage(String text) {
        addChatMessage(newComponent(text));
    }

    public static void addChatMessage(String text, String url, String hoverText) {
        addChatMessage(newComponent(text, url, hoverText));
    }

    private static void addChatMessage(Object component) {
        EntityPlayerSP thePlayer = autotip.getMinecraft().thePlayer;
        thePlayer.addChatMessage((IChatComponent) component);
    }

    private static IChatComponent newComponent(String text) {
        return new ChatComponentText(text);
    }

    private static IChatComponent newComponent(String text, String url, String hoverText) {
        ChatStyle chatStyle = new ChatStyle();
        ClickEvent clickEvent;
        HoverEvent hoverEvent;

        if (url != null && !url.equals("")) {
            clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
            chatStyle.setChatClickEvent(clickEvent);
        }

        if (hoverText != null && !hoverText.equals("")) {
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, newComponent(hoverText));
            chatStyle.setChatHoverEvent(hoverEvent);
        }

        IChatComponent chatComponent = newComponent(text);
        chatComponent.setChatStyle(chatStyle);
        return chatComponent;
    }
}
