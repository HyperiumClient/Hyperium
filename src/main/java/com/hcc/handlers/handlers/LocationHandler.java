/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.handlers.handlers;

import com.hcc.HCC;
import com.hcc.config.ConfigOpt;
import com.hcc.event.ChatEvent;
import com.hcc.event.InvokeEvent;
import com.hcc.event.SpawnpointChangeEvent;
import com.hcc.event.TickEvent;
import com.hcc.mods.sk1ercommon.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationHandler {

    @ConfigOpt
    private String location = "";
    private Pattern whereami = Pattern.compile("You are currently connected to server (?<server>.+)");
    private boolean sendingWhereAmI = false;
    private long ticksInWorld = 0;

    @InvokeEvent
    public void chatRecieve(ChatEvent event) {

        String raw = ChatColor.stripColor(event.getChat().getUnformattedText());
        Matcher whereAmIMatcher = whereami.matcher(raw);
        if (raw.equalsIgnoreCase("you are currently in limbo")) {
            this.location = "Limbo";
            if (sendingWhereAmI) {
                sendingWhereAmI = false;
                event.setCancelled(true);
            }

            return;
        }
        if (!whereAmIMatcher.matches()) {
            return;
        }

        this.location = whereAmIMatcher.group("server");
        if (sendingWhereAmI) {
            sendingWhereAmI = false;
            event.setCancelled(true);
        }

    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (!HCC.INSTANCE.getHandlers().getHypixelDetector().isHypixel())
            ticksInWorld = 0;
        if (ticksInWorld < 20) {
            ticksInWorld++;
            if (ticksInWorld == 20) {
                HCC.INSTANCE.getHandlers().getCommandQueue().queue("/whereami");
                sendingWhereAmI = true;
            }
        }

    }

    @InvokeEvent
    public void swapWorld(SpawnpointChangeEvent event) {
        ticksInWorld = 0;
    }

    public String getLocation() {
        return location;
    }
}
