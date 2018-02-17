package me.semx11.autotip.util;

public class ClientMessage {

    private static String prefix =
            ChatColor.GOLD + "A" + ChatColor.YELLOW + "T" + ChatColor.DARK_GRAY + " > "
                    + ChatColor.GRAY;

    public static void send(String msg) {
        UniversalUtil.chatMessage(prefix + msg);
    }

    public static void send(String msg, String url, String hoverText) {
        UniversalUtil.chatMessage(prefix + msg, url, hoverText);
    }

    public static void sendRaw(String msg) {
        UniversalUtil.chatMessage(msg);
    }

    public static void separator() {
        UniversalUtil.chatMessage(
                ChatColor.GOLD + "" + ChatColor.BOLD + "----------------------------------");
    }

}
