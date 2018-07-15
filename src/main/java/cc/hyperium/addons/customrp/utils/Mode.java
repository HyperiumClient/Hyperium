package cc.hyperium.addons.customrp.utils;

import cc.hyperium.Hyperium;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.addons.customrp.config.Config;

import java.awt.*;
import java.util.function.Supplier;

public class Mode {
    public static Supplier<String[]> modes = new Supplier<String[]>() {
        @Override
        public String[] get() {
            String[] internalModes = {"addon", "eVowels", "eAll", "sellout", "bestCoder", "merch", "respects", "sleepy"};
            return internalModes;
        }
    };

    public static void set(String mode) {
        switch (mode.toLowerCase()) {
            case "addon":
                Config.customRPMode = "addon";
                RichPresenceUpdater.callCustomRPUpdate();
                Hyperium.INSTANCE.getNotification().display("CustomRP", "Mode set to\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
                break;
            case "evowels":
                Config.customRPMode = "eVowels";
                RichPresenceUpdater.callCustomRPUpdate();
                Hyperium.INSTANCE.getNotification().display("CustomRP", "Mode set to\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
                break;
            case "eall":
                Config.customRPMode = "eAll";
                RichPresenceUpdater.callCustomRPUpdate();
                Hyperium.INSTANCE.getNotification().display("CustomRP", "Mode set to\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
                break;
            case "sellout":
                Config.customRPMode = "sellout";
                RichPresenceUpdater.callCustomRPUpdate();
                Hyperium.INSTANCE.getNotification().display("CustomRP", "Mode set to\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
                break;
            case "bestcoder":
                Config.customRPMode = "bestCoder";
                RichPresenceUpdater.callCustomRPUpdate();
                Hyperium.INSTANCE.getNotification().display("CustomRP", "Mode set to\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
                break;
            case "merch":
                Config.customRPMode = "merch";
                RichPresenceUpdater.callCustomRPUpdate();
                Hyperium.INSTANCE.getNotification().display("CustomRP", "Mode set to\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
                break;
            case "respects":
                Config.customRPMode = "respects";
                RichPresenceUpdater.callCustomRPUpdate();
                Hyperium.INSTANCE.getNotification().display("CustomRP", "Mode set to\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
                break;
            case "sleepy":
                Config.customRPMode = "sleepy";
                RichPresenceUpdater.callCustomRPUpdate();
                Hyperium.INSTANCE.getNotification().display("CustomRP", "Mode set to\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
                break;
            default:
                Hyperium.INSTANCE.getNotification().display("CustomRP", "Invalid mode:\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
                break;
        }
    }

    public static void preview(String mode) {
        switch (mode.toLowerCase()) {
            case "addon":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP Addon v[version]\n" +
                                "&bPlaying a game\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "evowels":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCestemRP edden v[version]\n" +
                                "&beGN: [IGN but vowels are es]\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "eall":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&beeeeeeee eeeee e[version]\n" +
                                "&beee: [IGN but every character is e]\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "sellout":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP Addon v[version]\n" +
                                "&bSubscribe to [IGN]\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "bestcoder":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP Addon v[version]\n" +
                                "&b[IGN] best coder\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "merch":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP Addon v[version]\n" +
                                "&bBuy [IGN]'s merch\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "respects":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP Addon v[version]\n" +
                                "&bPress [F] to pay respects\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "sleepy":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP Addon v[version]\n" +
                                "&b[IGN] sleepy\n" +
                                "&b[time]\n")
                        , false);
                break;
            default:
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP Addon v[version]\n" +
                                "&bPlaying a game\n" +
                                "&b[time]\n")
                        , false);
                break;
        }
    }
}
