/*
 * Hypixel Community Client, Client optimized for Hypixel Network
 * Copyright (C) 2018  HCC Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.event.minigames

import com.hcc.event.EventBus
import com.hcc.event.InvokeEvent
import com.hcc.event.JoinMinigameEvent
import com.hcc.event.TickEvent
import net.minecraft.client.Minecraft

class MinigameListener {

    var cooldown = 3 * 20

    var server = ""

    var currentMinigameName = ""


    @InvokeEvent
    fun onTick(event: TickEvent) {
        if (server == "hypixel" && Minecraft.getMinecraft().theWorld.scoreboard != null) {
            if (cooldown <= 0) {
                cooldown = 3 * 20
                currentMinigameName = getScoreboardTitle()
                val minigames = Minigame.values()
                minigames.forEach {
                    if (currentMinigameName == it.name) {
                        EventBus.post(JoinMinigameEvent(it))
                    }
                }

            }
            else {
                cooldown--
            }

        }
    }

    private fun getScoreboardTitle(): String {
        if (Minecraft.getMinecraft().theWorld.scoreboard.getObjectiveInDisplaySlot(1) != null) {
            return Minecraft.getMinecraft().theWorld.scoreboard
                    .getObjectiveInDisplaySlot(1)
                    .displayName.trim()
                    .replace(Regex("\u00A7[0-9a-zA-Z]"), "")
        }
        return ""
    }

}