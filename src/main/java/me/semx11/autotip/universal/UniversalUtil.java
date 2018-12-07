package me.semx11.autotip.universal;

import cc.hyperium.event.ServerChatEvent;
import cc.hyperium.event.ServerJoinEvent;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.chat.ChatColor;
import me.semx11.autotip.util.ErrorReport;
import me.semx11.autotip.util.MinecraftVersion;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumChatFormatting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketAddress;

import static me.semx11.autotip.universal.ReflectionUtil.findClazz;
import static me.semx11.autotip.universal.ReflectionUtil.findMethod;
import static me.semx11.autotip.universal.ReflectionUtil.getConstructor;
import static me.semx11.autotip.universal.ReflectionUtil.getEnum;

public class UniversalUtil {

    private static Autotip autotip;

    private static Class<?> componentClass;
    private static Class<?> textComponentClass;

    private static Method addChatMethod;

    private static Class<?> chatStyleClass;

    private static Class<?> clickEventClass;
    private static Class<?> clickEventActionClass;

    private static Class<?> hoverEventClass;
    private static Class<?> hoverEventActionClass;

    static {
        componentClass = findClazz(
                "net.minecraft.util.IChatComponent", // 1.8 - 1.8.9
                "net.minecraft.util.text.ITextComponent" // 1.9 - 1.12.2
        );
        textComponentClass = findClazz(
                "net.minecraft.util.ChatComponentText", // 1.8 - 1.8.9
                "net.minecraft.util.text.TextComponentString" // 1.9 - 1.12.2
        );
        addChatMethod = findMethod(
                EntityPlayerSP.class,
                new String[]{
                        "func_145747_a", // 1.8  - 1.8.9  | 1.11 - 1.12.2
                        "func_146105_b", // 1.9  - 1.10.2
                        "addChatMessage", // 1.8  - 1.8.9  | 1.11 - 1.12.2
                        "addChatComponentMessage", // 1.9  - 1.10.2
                        "sendMessage" // 1.11 - 1.12.2
                },
                componentClass
        );
        chatStyleClass = findClazz(
                "net.minecraft.util.ChatStyle", // 1.8 - 1.8.9
                "net.minecraft.util.text.Style" // 1.9 - 1.12.2
        );
        clickEventClass = findClazz(
                "net.minecraft.event.ClickEvent", // 1.8 - 1.8.9
                "net.minecraft.util.text.event.ClickEvent" // 1.9 - 1.12.2
        );
        clickEventActionClass = findClazz(
                "net.minecraft.event.ClickEvent$Action", // 1.8 - 1.8.9
                "net.minecraft.util.text.event.ClickEvent$Action" // 1.9 - 1.12.2
        );
        hoverEventClass = findClazz(
                "net.minecraft.event.HoverEvent", // 1.8 - 1.8.9
                "net.minecraft.util.text.event.HoverEvent" // 1.9 - 1.12.2
        );
        hoverEventActionClass = findClazz(
                "net.minecraft.event.HoverEvent$Action", // 1.8 - 1.8.9
                "net.minecraft.util.text.event.HoverEvent$Action" // 1.9 - 1.12.2
        );
    }

    public static void setAutotip(Autotip autotip) {
        UniversalUtil.autotip = autotip;
    }

    public static MinecraftVersion getMinecraftVersion() {

        return MinecraftVersion.V1_8_8;
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
        try {
            String text = (String) findMethod(
                    componentClass,
                    new String[]{"func_150260_c", "getUnformattedText"}
            ).invoke(component);
            return ChatColor.stripFormatting(text);
        } catch (IllegalAccessException | InvocationTargetException e) {
            ErrorReport.reportException(e);
            return null;
        }
    }

    public static String getHoverText(ServerChatEvent event) {
        return getHoverText(event.getChat());
    }

    public static String getHoverText(Object component) {
        if (component == null) {
            return null;
        }
        try {
            Object style = findMethod(
                    componentClass,
                    new String[]{"func_150256_b", "getChatStyle"}
            ).invoke(component);
            Object hoverEvent = findMethod(
                    chatStyleClass,
                    new String[]{"func_150210_i", "getChatHoverEvent"}
            ).invoke(style);
            if (hoverEvent == null) {
                return null;
            }
            Object hoverComponent = findMethod(
                    hoverEventClass,
                    new String[]{"func_150702_b", "getValue"}
            ).invoke(hoverEvent);
            return getUnformattedText(hoverComponent);
        } catch (InvocationTargetException | IllegalAccessException e) {
            ErrorReport.reportException(e);
            return "";
        }
    }

    public static String getFormattedText(Object component) {
        try {
            return (String) findMethod(
                    componentClass,
                    new String[]{"func_150254_d", "getFormattedText"}
            ).invoke(component);
        } catch (IllegalAccessException | InvocationTargetException e) {
            ErrorReport.reportException(e);
            return "";
        }
    }

    public static void addChatMessage(String text) {
        addChatMessage(newComponent(text));
    }

    public static void addChatMessage(String text, String url, String hoverText) {
        addChatMessage(newComponent(text, url, hoverText));
    }

    private static void addChatMessage(Object component) {
        EntityPlayerSP thePlayer = autotip.getMinecraft().thePlayer;
        try {
            addChatMethod.invoke(thePlayer, component);
        } catch (InvocationTargetException | IllegalAccessException e) {
            ErrorReport.reportException(e);
        }
    }

    private static Object newComponent(String text) {
        try {
            return getConstructor(textComponentClass, String.class).newInstance(text);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ErrorReport.reportException(e);
            return null;
        }
    }

    private static Object newComponent(String text, String url, String hoverText) {
        try {
            Object chatStyle = chatStyleClass.newInstance();
            Object clickEvent;
            Object hoverEvent;

            if (url != null && !url.equals("")) {
                clickEvent = getConstructor(
                        clickEventClass,
                        clickEventActionClass,
                        String.class
                ).newInstance(getEnum(clickEventActionClass, "OPEN_URL"), url);

                findMethod(
                        chatStyleClass,
                        new String[]{"func_150241_a", "setChatClickEvent"}, // 1.8 - 1.12.2
                        clickEventClass
                ).invoke(chatStyle, clickEvent);
            }

            if (hoverText != null && !hoverText.equals("")) {
                hoverEvent = getConstructor(
                        hoverEventClass,
                        hoverEventActionClass,
                        componentClass
                ).newInstance(getEnum(hoverEventActionClass, "SHOW_TEXT"), newComponent(hoverText));

                findMethod(
                        chatStyleClass,
                        new String[]{"func_150209_a", "setChatHoverEvent"}, // 1.8 - 1.12.2
                        hoverEventClass
                ).invoke(chatStyle, hoverEvent);
            }

            Object chatComponent = newComponent(text);

            return findMethod(
                    textComponentClass,
                    new String[]{"func_150255_a", "setChatStyle"}, // 1.8 - 1.12.2
                    chatStyleClass
            ).invoke(chatComponent, chatStyle);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ErrorReport.reportException(e);
            return null;
        }
    }

    private static boolean isLegacy() {
        switch (autotip.getMcVersion()) {
            case V1_8:
            case V1_8_8:
            case V1_8_9:
                return true;
            case V1_9:
            case V1_9_4:
            case V1_10:
            case V1_10_2:
            case V1_11:
            case V1_11_2:
            case V1_12:
            case V1_12_1:
            case V1_12_2:
                return false;
            default:
                return false;
        }
    }

}
