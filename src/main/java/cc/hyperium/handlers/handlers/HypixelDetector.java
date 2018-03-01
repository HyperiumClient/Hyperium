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

package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.event.*;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import java.util.regex.Pattern;

public class HypixelDetector {

    private static final Pattern HYPIXEL_PATTERN =
            Pattern.compile("^(?:(?:(?:\\w+\\.)?hypixel\\.net)|(?:209\\.222\\.115\\.\\d{1,3}))(?::\\d{1,5})?$", Pattern.CASE_INSENSITIVE);
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
        boolean h1 = this.hypixel;
        this.hypixel = HYPIXEL_PATTERN.matcher(event.getServer()).find();
        if (hypixel) {
            Multithreading.runAsync(() -> {
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
                EventBus.INSTANCE.post(new JoinHypixelEvent());
            });


        }
    }

    @InvokeEvent
    public void join(JoinHypixelEvent event) {
        System.out.println("Zoo");
        Hyperium.INSTANCE.getNotification().display("Hypixel", "Welcome to the HYPIXEL ZOO", 5f);

        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("zoo"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));



//        Minecraft.getMinecraft().thePlayer.playSound("hyperium:zoo",1.0F,1.0F);
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
