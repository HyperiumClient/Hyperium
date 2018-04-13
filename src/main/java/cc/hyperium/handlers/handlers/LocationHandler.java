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
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.*;
import cc.hyperium.event.minigames.Minigame;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.UpdateLocationPacket;
import cc.hyperium.utils.ChatColor;

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
        if(!Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel())
            return;
        String raw = ChatColor.stripColor(event.getChat().getUnformattedText());
        Matcher whereAmIMatcher = whereami.matcher(raw);
        if (raw.equalsIgnoreCase("you are currently in limbo")) {
            EventBus.INSTANCE.post(new ServerSwitchEvent(this.location, "Limbo"));
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

        String to = whereAmIMatcher.group("server");
        String old = this.location;
        if (!this.location.equalsIgnoreCase(to)) {
            EventBus.INSTANCE.post(new ServerSwitchEvent(old, to));
        }
        this.location = to;
        if (sendingWhereAmI) {
            sendingWhereAmI = false;
            event.setCancelled(true);
        }

    }

    @InvokeEvent
    public void serverSwap(ServerSwitchEvent event) {
        if (Hyperium.INSTANCE.getMinigameListener().getScoreboardTitle().equalsIgnoreCase(Minigame.HOUSING.name()))
            NettyClient.getClient().write(UpdateLocationPacket.build(Minigame.HOUSING.name()));
        else
            NettyClient.getClient().write(UpdateLocationPacket.build(event.getTo()));
    }

    @InvokeEvent
    public void miniGameJoin(JoinMinigameEvent event) {
        if (event.getMinigame() == Minigame.HOUSING)
            NettyClient.getClient().write(UpdateLocationPacket.build(Minigame.HOUSING.name()));
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        Hyperium instance = Hyperium.INSTANCE;
        HyperiumHandlers handlers = instance.getHandlers();
        if (handlers == null) return;
        HypixelDetector hypixelDetector = handlers.getHypixelDetector();
        if (!hypixelDetector.isHypixel())
            ticksInWorld = 0;
        if (ticksInWorld < 20) {
            ticksInWorld++;
            if (ticksInWorld == 20) {
                Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/whereami");
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
