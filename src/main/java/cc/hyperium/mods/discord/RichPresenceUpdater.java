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
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.JoinMinigameEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.event.ServerLeaveEvent;
import cc.hyperium.event.SingleplayerJoinEvent;
import cc.hyperium.gui.settings.items.GeneralSetting;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;
import net.minecraft.client.Minecraft;

import java.time.OffsetDateTime;

public class RichPresenceUpdater {

    private final IPCClient client;

    RichPresenceUpdater(IPCClient client) {
        this.client = client;
        if (!GeneralSetting.discordRPEnabled) return;

        RichPresence.Builder builder = new RichPresence.Builder();
        client.sendRichPresence(builder
                .setSmallImage("compass")
                .setLargeImage("hyperium", "Hyperium Client")
                .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                .setDetails("On the Main Menu")
                .setStartTimestamp(OffsetDateTime.now())
                .build());
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onServerJoin(ServerJoinEvent event) {
        if (!GeneralSetting.discordRPEnabled || !GeneralSetting.discordServerDisplayEnabled) return;
        RichPresence.Builder builder = new RichPresence.Builder();
        if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("16", "Hypixel Network")
                    .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setDetails("In the Lobbies on MC.HYPIXEL.NET")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        } else {
            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("16", "Hypixel Network")
                    .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setDetails("On a Minecraft server")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        }
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onMinigameJoin(JoinMinigameEvent event) {
        if (!GeneralSetting.discordRPEnabled || !GeneralSetting.discordServerDisplayEnabled) return;
        RichPresence.Builder builder = new RichPresence.Builder();
        client.sendRichPresence(builder
                .setSmallImage("compass")
                .setLargeImage(String.valueOf(event.getMinigame().getId()), event.getMinigame().getScoreName())
                .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                .setDetails("Playing " + event.getMinigame().getScoreName() + " on MC.HYPIXEL.NET")
                .setStartTimestamp(OffsetDateTime.now())
                .build());
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onSinglePlayer(SingleplayerJoinEvent event) {
        if (!GeneralSetting.discordRPEnabled) return;
        RichPresence.Builder builder = new RichPresence.Builder();
        client.sendRichPresence(builder
                .setSmallImage("compass")
                .setLargeImage("hyperium", "Hyperium Client")
                .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                .setDetails("Playing Single-Player")
                .setStartTimestamp(OffsetDateTime.now())
                .build());
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onServerLeave(ServerLeaveEvent e) {
        if (!GeneralSetting.discordRPEnabled) return;
        RichPresence.Builder builder = new RichPresence.Builder();
        client.sendRichPresence(builder
                .setSmallImage("compass")
                .setLargeImage("hyperium", "Hyperium Client")
                .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                .setDetails("On the Main Menu")
                .setStartTimestamp(OffsetDateTime.now())
                .build());
    }
}
