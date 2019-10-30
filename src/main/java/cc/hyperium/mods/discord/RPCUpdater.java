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

package cc.hyperium.mods.discord;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.ServerJoinEvent;
import cc.hyperium.event.network.server.ServerLeaveEvent;
import cc.hyperium.event.network.server.SingleplayerJoinEvent;
import cc.hyperium.event.network.server.hypixel.JoinMinigameEvent;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;
import net.minecraft.client.Minecraft;

import java.time.OffsetDateTime;

public class RPCUpdater {

    private final IPCClient client;

    RPCUpdater(IPCClient client) {
        this.client = client;

        if (!Settings.DISCORD_RP) return;

        RichPresence.Builder builder = new RichPresence.Builder();

        client.sendRichPresence(builder
            .setSmallImage("compass")
            .setLargeImage("hyperium", "Hyperium Client")
            .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
            .setDetails("On the main menu")
            .setStartTimestamp(OffsetDateTime.now())
            .build());
    }

    @InvokeEvent
    public void joinServer(ServerJoinEvent event) {
        if (Settings.DISCORD_RP_SERVER) {
            RichPresence.Builder builder = new RichPresence.Builder();

            client.sendRichPresence(Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel() ?
                builder
                    .setSmallImage("compass")
                    .setLargeImage("16", "Hypixel Network")
                    .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setDetails("In the lobby on hypixel.net")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build() :
                builder
                    .setSmallImage("compass")
                    .setLargeImage("16", "On a server")
                    .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setDetails("On a Minecraft server")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        }
    }

    @InvokeEvent
    public void joinHypixelMinigame(JoinMinigameEvent event) {
        if (Settings.DISCORD_RP_SERVER) {
            RichPresence.Builder builder = new RichPresence.Builder();

            client.sendRichPresence(builder
                .setSmallImage("compass")
                .setLargeImage(String.valueOf(event.getMinigame().getId()), event.getMinigame().getScoreName())
                .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                .setDetails("Playing " + event.getMinigame().getScoreName() + " on hypixel.net")
                .setStartTimestamp(OffsetDateTime.now())
                .build());
        }
    }

    @InvokeEvent
    public void joinSingleplayer(SingleplayerJoinEvent event) {
        RichPresence.Builder builder = new RichPresence.Builder();

        client.sendRichPresence(builder
            .setSmallImage("compass")
            .setLargeImage("hyperium", "Hyperium Client")
            .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
            .setDetails("Playing Singleplayer")
            .setStartTimestamp(OffsetDateTime.now())
            .build());
    }

    @InvokeEvent
    public void leaveServer(ServerLeaveEvent event) {
        RichPresence.Builder builder = new RichPresence.Builder();

        client.sendRichPresence(builder
            .setSmallImage("compass")
            .setLargeImage("hyperium", "Hyperium Client")
            .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
            .setDetails("On the main menu")
            .setStartTimestamp(OffsetDateTime.now())
            .build());
    }
}
