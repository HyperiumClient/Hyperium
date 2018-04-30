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

package cc.hyperium.event.minigames

import cc.hyperium.Hyperium
import cc.hyperium.event.EventBus
import cc.hyperium.event.InvokeEvent
import cc.hyperium.event.JoinMinigameEvent
import cc.hyperium.event.TickEvent
import net.minecraft.client.Minecraft

class MinigameListener {

    private var cooldown = 3 * 20

    var currentMinigameName = ""

    @InvokeEvent
    fun onTick(event: TickEvent) {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (Hyperium.INSTANCE.handlers.hypixelDetector.isHypixel && Minecraft.getMinecraft().theWorld.scoreboard != null) {
                if (cooldown <= 0) {
                    cooldown = 3 * 20
                    val minigameName = getScoreboardTitle()
                    val minigames = Minigame.values()
                    minigames.forEach {
                        if (minigameName.equals(it.scoreName, true) && !minigameName.equals(currentMinigameName, true)) {
                            currentMinigameName = minigameName
                            EventBus.post(JoinMinigameEvent(it))
                        }
                    }
                } else {
                    cooldown--
                }
            }
        }
    }

    public fun getScoreboardTitle(): String {
        if (Minecraft.getMinecraft().theWorld.scoreboard.getObjectiveInDisplaySlot(1) != null) {
            return Minecraft.getMinecraft().theWorld.scoreboard
                    .getObjectiveInDisplaySlot(1)
                    .displayName.trim()
                    .replace(Regex("\u00A7[0-9a-zA-Z]"), "")
        }
        return ""
    }

}