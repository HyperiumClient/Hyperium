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

package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.JoinHypixelEvent;
import cc.hyperium.event.JoinHypixelEvent.ServerVerificationMethod;
import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.event.ServerLeaveEvent;
import cc.hyperium.event.SingleplayerJoinEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.ResourceLocation;
import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class HypixelDetector {

    private static final Pattern HYPIXEL_PATTERN =
        Pattern.compile("^(?:(?:(?:.+\\.)?hypixel\\.net)|(?:209\\.222\\.115\\.\\d{1,3})|(?:99\\.198\\.123\\.[123]?\\d?))\\.?(?::\\d{1,5}\\.?)?$", Pattern.CASE_INSENSITIVE);

    private static HypixelDetector instance;
    private boolean hypixel = false;

    public HypixelDetector() {
        instance = this;
    }

    public static HypixelDetector getInstance() {
        return instance;
    }

    @InvokeEvent
    public void serverJoinEvent(ServerJoinEvent event) {
        this.hypixel = HYPIXEL_PATTERN.matcher(event.getServer()).find();

        Multithreading.runAsync(() -> {
            // Wait a while until the player isn't null, signifying the joining process is complete
            int tries = 0;
            while (Minecraft.getMinecraft().thePlayer == null) {
                tries++;
                if (tries > 20 * 10) {
                    return;
                }
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (hypixel) { // If player is online recognized Hypixel IP
                EventBus.INSTANCE.post(new JoinHypixelEvent(ServerVerificationMethod.IP));

            } else { // Double check the player isn't online Hypixel
                if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().getCurrentServerData() != null) {
                    final ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();

                    if (serverData != null && serverData.serverMOTD != null) {
                        if (serverData.serverMOTD.toLowerCase().contains("hypixel network")) { // Check MOTD for Hypixel
                            this.hypixel = true;
                            EventBus.INSTANCE.post(new JoinHypixelEvent(ServerVerificationMethod.MOTD));
                        }
                    }
                }
            }
        });
    }

    @InvokeEvent
    public void join(JoinHypixelEvent event) {
        if (Settings.HYPIXEL_ZOO) {
            Hyperium.INSTANCE.getNotification().display("Welcome to the Hypixel Zoo.", "Click to visit https://hypixel.net/", 5f,
                null, () -> {
                    try {
                        Desktop.getDesktop().browse(new URI("https://hypixel.net/"));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }, new Color(200, 150, 50));

            SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
            if (soundHandler == null || Minecraft.getMinecraft().theWorld == null)
                return;
            soundHandler.playSound(PositionedSoundRecord.create(new ResourceLocation("zoo"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
        }
    }

    @InvokeEvent
    public void serverLeaveEvent(ServerLeaveEvent event) {
        hypixel = false;
    }

    @InvokeEvent
    public void singlePlayerJoin(SingleplayerJoinEvent event) {
        hypixel = false;
    }

    public boolean isHypixel() {
        return hypixel;
    }

}
