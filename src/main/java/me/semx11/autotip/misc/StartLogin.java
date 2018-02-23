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
