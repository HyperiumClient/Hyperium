package me.semx11.autotip.misc;

import me.semx11.autotip.Autotip;
import me.semx11.autotip.util.*;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class StartLogin implements Runnable {

    public static void login() {
        Versions.updateVersions();
        Host loginHost = Hosts.getInstance().getHostById("update");
        Host downloadHost = Hosts.getInstance().getHostById("download");
        if (loginHost != null && loginHost.isEnabled()) {
            try {
                String ignored = IOUtils.toString(new URL(String.format(
                        loginHost.getUrl(),
                        Minecraft.getMinecraft().thePlayer.getUniqueID(),
                        Autotip.VERSION.get(),
                        "1.8.9",
                        Autotip.totalTipsSent,
                        System.getProperty("os.name")
                )));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<VersionInfo> vInfo = Versions.getInstance().getHigherVersionInfo(Autotip.VERSION);
        if (vInfo.size() > 0) {
            ClientMessage.separator();
            ClientMessage.send(
                    ChatColor.RED + "Autotip is out of date! Click here to update.",
                    "https://" + downloadHost.getUrl(),
                    ChatColor.GRAY + "Click to visit " + ChatColor.GOLD + downloadHost.getUrl()
                            + ChatColor.GRAY + "!"
            );
            ClientMessage.send("Update info:");
            vInfo.forEach(vi -> {
                ClientMessage.send(ChatColor.GOLD + "Autotip v" + vi.getVersion());
                ClientMessage.send("Update severity: " + vi.getSeverity().toColoredString());
                vi.getChangelog().forEach(
                        s -> ClientMessage.send(ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + s));
            });
            ClientMessage.separator();
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            login();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
