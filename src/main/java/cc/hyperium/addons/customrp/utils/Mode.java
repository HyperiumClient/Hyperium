package cc.hyperium.addons.customrp.utils;

import cc.hyperium.Hyperium;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.addons.customrp.config.Config;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Supplier;

public class Mode {
    public static Supplier<String[]> modes = () -> new String[]{"Addon", "E-Vowels", "E-All", "Sellout", "BestCoder", "Merch", "Respects", "Sleepy"};

    public static void set(String mode) {
        if(mode ==null)
            return;
        if (!Arrays.asList(modes.get()).contains(mode))
            Hyperium.INSTANCE.getNotification().display("CustomRP", "Invalid mode:\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
        else {
            Config.CUSTOM_RP_MODE = mode;
            RichPresenceUpdater.callCustomRPUpdate();
            Hyperium.INSTANCE.getNotification().display("CustomRP", "Mode set to\n    '" + mode + "'", 1.5F, null, null, Color.CYAN);
        }
    }

    public static void preview(String mode) {
        if(mode == null)
            return;
        switch (mode.toLowerCase()) {
            case "addon":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP [Internal]\n" +
                                "&bPlaying a game\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "e-vowels":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCestemRP [enternel]\n" +
                                "&beGN: " + EUtils.geteIfiedUsername() + "\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "e-all":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&beeeeeeee [eeeeeeee]\n" +
                                "&beee: " + EUtils.getAllEUsername() + "\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "sellout":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP [Internal]\n" +
                                "&bSubscribe to " + EUtils.getUsername() + "\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "bestcoder":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP [Internal]\n" +
                                "&b" + EUtils.getUsername() + " best coder\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "merch":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP [Internal]\n" +
                                "&bBuy " + EUtils.getUsername() + "'s merch\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "respects":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP [Internal]\n" +
                                "&bPress [F] to pay respects\n" +
                                "&b[time]\n")
                        , false);
                break;
            case "sleepy":
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP [Internal]\n" +
                                "&b" + EUtils.getUsername() + " sleepy\n" +
                                "&b[time]\n")
                        , false);
                break;
            default:
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fPreview of '" + mode + "'\n\n" +
                                "&cHyperium\n" +
                                "&bCustomRP [Internal]\n" +
                                "&bPlaying a game\n" +
                                "&b[time]\n")
                        , false);
                break;
        }
    }
}
