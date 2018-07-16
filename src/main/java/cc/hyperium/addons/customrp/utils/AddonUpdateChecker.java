package cc.hyperium.addons.customrp.utils;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.event.SingleplayerJoinEvent;
import cc.hyperium.gui.NotificationCenter;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.utils.ChatColor;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

public class AddonUpdateChecker {
    static String updateVersion;
    static String localVersion;

    public static String getLocalVersion() {
        AddonBootstrap.INSTANCE.getAddonManifests().forEach(m -> {
            if (m.getName().equalsIgnoreCase("CustomRP addon")) {
                localVersion = m.getVersion();
                localVersion = localVersion.substring(0, 5);
            }
        });

        return localVersion;
    }

    public static void getVersion() {
        try {
            updateVersion = IOUtils.toString(
                    new URL("https://raw.githubusercontent.com/SHARDcoder/CustomRPAddon/master/version.txt"),
                    Charset.forName("UTF-8")
            );

            updateVersion = updateVersion.substring(0, 5);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void updateCheck() {
        getVersion();
        AddonBootstrap.INSTANCE.getAddonManifests().forEach(m -> {
            if (m.getName().equalsIgnoreCase("CustomRP addon")) {
                localVersion = m.getVersion();
                localVersion = localVersion.substring(0, 5);

                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fRunning version: " + localVersion), false);
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[CustomRP] &fLatest version: " + updateVersion), false);

                if (!localVersion.equalsIgnoreCase(updateVersion)) {
                    System.out.println("[CustomRP] Outdated version detected. Suggesting update");
                    Hyperium.INSTANCE.getNotification().display("CustomRP", "New version available\n    Click to download", 10F, null, () -> {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().browse(new URI("https://github.com/SHARDcoder/CustomRPAddon/releases/download/v" + updateVersion + "/Addon.CustomRPAddon.v" + updateVersion + ".jar"));
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }, Color.RED);
                } else {
                    System.out.println("[CustomRP] Version up-to-date. No update needed");
                }
            }
        });
    }

    @InvokeEvent
    public void serverJoin(ServerJoinEvent event) {
        updateCheck();
    }

    @InvokeEvent
    public void lonelyJoin(SingleplayerJoinEvent event) {
        updateCheck();
    }
}
