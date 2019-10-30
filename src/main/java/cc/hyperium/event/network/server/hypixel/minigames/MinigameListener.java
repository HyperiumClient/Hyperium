/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.event.network.server.hypixel.minigames;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.hypixel.JoinMinigameEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

public class MinigameListener {

    private int cooldown = 3 * 20;
    private String currentMinigameName = "";

    @InvokeEvent
    public void onTick(TickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null && Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel() &&
            Minecraft.getMinecraft().theWorld.getScoreboard() != null) {
            if (cooldown <= 0) {
                cooldown = 3 * 20;
                String minigameName = getScoreboardTitle();
                Arrays.stream(Minigame.values()).filter(m -> minigameName.equalsIgnoreCase(m.scoreName) &&
                    !minigameName.equalsIgnoreCase(currentMinigameName)).forEach(m -> {
                    currentMinigameName = minigameName;
                    EventBus.INSTANCE.post(new JoinMinigameEvent(m));
                });
            } else {
                cooldown--;
            }
        }
    }

    public String getScoreboardTitle() {
        return Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null ?
            ChatColor.stripColor(Minecraft.getMinecraft().theWorld.getScoreboard()
                .getObjectiveInDisplaySlot(1)
                .getDisplayName().trim()
                .replace("\u00A7[0-9a-zA-Z]", "")) : "";
    }

    public String getCurrentMinigameName() {
        return currentMinigameName;
    }
}
