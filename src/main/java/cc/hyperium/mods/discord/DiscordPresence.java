/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.discord;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.GuiOpenEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.JoinMinigameEvent;
import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.event.SingleplayerJoinEvent;
import cc.hyperium.gui.GuiHyperiumScreenMainMenu;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;

public class DiscordPresence {

    private long startTime;

    public void load() {
        if (Settings.DISCORD_RP) {
            EventBus.INSTANCE.register(this);
            startTime = System.currentTimeMillis();
            DiscordRPC.discordInitialize("412963310867054602L", new DiscordEventHandlers(), true);

            new Thread(() -> {
                while (true) {
                    DiscordRPC.discordRunCallbacks();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void shutdown() {
        DiscordRPC.discordClearPresence();
        DiscordRPC.discordShutdown();
    }

    @InvokeEvent
    private void onDisplayGui(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiHyperiumScreenMainMenu) {
            DiscordRPC.discordUpdatePresence(
                new DiscordRichPresence.Builder("On the Main Menu")
                    .setDetails("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setStartTimestamps(startTime)
                    .setSmallImage("compass", "Hyperium")
                    .setBigImage("hyperium", "Hyperium Client")
                    .build()
            );
        } else if (event.getGui() instanceof GuiMultiplayer) {
            DiscordRPC.discordUpdatePresence(
                new DiscordRichPresence.Builder("Browsing Servers")
                    .setDetails("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setStartTimestamps(startTime)
                    .setSmallImage("compass", "Hyperium")
                    .setBigImage("hyperium", "Hyperium Client")
                    .build()
            );
        } else if (event.getGui() instanceof GuiSelectWorld) {
            DiscordRPC.discordUpdatePresence(
                new DiscordRichPresence.Builder("Selecting a World")
                    .setDetails("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setStartTimestamps(startTime)
                    .setSmallImage("compass", "Hyperium")
                    .setBigImage("hyperium", "Hyperium Client")
                    .build()
            );
        }
    }

    @InvokeEvent
    private void onServerJoin(ServerJoinEvent event) {
        if (Settings.DISCORD_RP_SERVER) {
            if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
                DiscordRPC.discordUpdatePresence(
                new DiscordRichPresence.Builder("Playing on Hypixel")
                    .setDetails("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setStartTimestamps(startTime)
                    .setSmallImage("compass", "Hypixel Network")
                    .setBigImage("16", "Hypixel Network")
                    .build()
                );
            } else {
                DiscordRPC.discordUpdatePresence(
                    new DiscordRichPresence.Builder("On a Server")
                        .setDetails("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                        .setStartTimestamps(startTime)
                        .setSmallImage("compass", "Hyperium")
                        .setBigImage("hyperium", "Hyperium Client")
                        .build()
                );
            }
        }
    }

    @InvokeEvent
    public void onMinigameJoin(JoinMinigameEvent event) {
        if (Settings.DISCORD_RP_SERVER) {
            DiscordRPC.discordUpdatePresence(
                new DiscordRichPresence.Builder("Playing " + event.getMinigame().getScoreName() + " on Hypixel")
                    .setDetails("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setStartTimestamps(startTime)
                    .setSmallImage("compass", "Minigames")
                    .setBigImage(String.valueOf(event.getMinigame().getId()), event.getMinigame().getScoreName())
                    .build()
            );
        }
    }

    @InvokeEvent
    public void singleplayer(SingleplayerJoinEvent event) {
        DiscordRPC.discordUpdatePresence(
            new DiscordRichPresence.Builder("Playing Singleplayer")
                .setDetails("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                .setStartTimestamps(startTime)
                .setSmallImage("compass", "Singleplayer")
                .setBigImage("hyperium", "Hyperium Client")
                .build()
        );
    }
}
