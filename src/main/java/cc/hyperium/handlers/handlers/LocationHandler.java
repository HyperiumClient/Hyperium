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
import cc.hyperium.event.JoinMinigameEvent;
import cc.hyperium.event.ServerChatEvent;
import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.event.ServerLeaveEvent;
import cc.hyperium.event.ServerSwitchEvent;
import cc.hyperium.event.SpawnpointChangeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.event.minigames.Minigame;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.netty.packet.packets.serverbound.UpdateLocationPacket;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationHandler {

    private final Pattern whereami = Pattern.compile("You are currently connected to server (?<server>.+)");
    private String location = "";
    private boolean sendingWhereAmI = false;
    private long ticksInWorld = 0;

    @InvokeEvent
    public void serverJoinEvent(ServerJoinEvent event) {
        NettyClient client = NettyClient.getClient();
        if (client != null) {
            this.location = event.getServer();
            client.write(UpdateLocationPacket.build("Other"));
            if (Settings.SEND_SERVER)
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("server_update", event.getServer() + ":" + event.getPort())));

        }
    }

    @InvokeEvent
    public void serverLeaveEvent(ServerLeaveEvent event) {
        this.location = "Offline";
        NettyClient client = NettyClient.getClient();
        if (client != null) {
            client.write(UpdateLocationPacket.build("offline"));
            if (Settings.SEND_SERVER)
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("server_update", "Offline")));
        }
    }

    @InvokeEvent
    public void chatRecieve(ServerChatEvent event) {
        if (!Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel())
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
        if (NettyClient.getClient() == null) {
            return;
        }

        if (Hyperium.INSTANCE.getMinigameListener().getScoreboardTitle().equalsIgnoreCase(Minigame.HOUSING.name()))
            NettyClient.getClient().write(UpdateLocationPacket.build(Minigame.HOUSING.name()));
        else
            NettyClient.getClient().write(UpdateLocationPacket.build(event.getTo()));
        if (Hyperium.INSTANCE.getHandlers().getFlipHandler().getSelf() != 0)
            NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "flip_update").put("flip_state", 1)));

    }

    @InvokeEvent
    public void miniGameJoin(JoinMinigameEvent event) {
        if (NettyClient.getClient() == null) {
            return;
        }

        if (event.getMinigame() == Minigame.HOUSING) {
            NettyClient.getClient().write(UpdateLocationPacket.build(Minigame.HOUSING.name()));
            if (Hyperium.INSTANCE.getHandlers().getFlipHandler().getSelf() != 0)
                NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "flip_update").put("flip_state", 2)));

        }
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
                sendingWhereAmI = true;
                Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/whereami");
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

    public boolean isLobbyOrHousing() {
        return Hyperium.INSTANCE.getMinigameListener().getCurrentMinigameName().equalsIgnoreCase("HOUSING") || getLocation().contains("lobby");
    }
}
