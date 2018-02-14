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

package com.hcc.mods.discord;

import com.hcc.HCC;
import com.hcc.Metadata;
import com.hcc.mods.chromahud.Multithreading;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import net.minecraft.client.Minecraft;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

public class RichPresenceManager {

    private IPCClient client;


    public void init() {
        client = new IPCClient(412963310867054602L);
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                Multithreading.schedule(RichPresenceManager.this::update, 0L, 15L, TimeUnit.SECONDS);
                update();
            }
        });
        try {
            client.connect(DiscordBuild.ANY);
        } catch (NoDiscordClientException e) {
            e.printStackTrace();
            HCC.logger.warn("no discord clients found");
        }
    }

    public void update() {
        RichPresence presence;
        if (Minecraft.getMinecraft().theWorld == null)
            presence = new RichPresence(
                    "HCC " + Metadata.getVersion(),
                    "In main menu",
                    OffsetDateTime.now(),
                    null,
                    "hcc",
                    "HypixelCommunityClient",
                    "compass",
                    "AFK",
                    null,
                    0,
                    0,
                    null,
                    null,
                    null,
                    false
            );
        else if (Minecraft.getMinecraft().getCurrentServerData() != null) {

            presence = processHypixel();
            if (presence == null)
                //TODO add config setting to hide servers / add whitelist / blacklist
                presence = new RichPresence(
                        "HCC " + Metadata.getVersion(),
                        "In server: " + Minecraft.getMinecraft().getCurrentServerData().serverIP,
                        OffsetDateTime.now(),
                        null,
                        "hcc",
                        "HypixelCommunityClient",
                        "compass",
                        "In server",
                        null,
                        0,
                        0,
                        null,
                        null,
                        null,
                        false
                );
        } else
            presence = new RichPresence(
                    "HCC " + Metadata.getVersion(),
                    "Singleplayer",
                    OffsetDateTime.now(),
                    null,
                    "hcc",
                    "HypixelCommunityClient",
                    "compass",
                    "Singleplayer",
                    null,
                    0,
                    0,
                    null,
                    null,
                    null,
                    false
            );

        client.sendRichPresence(presence);
    }

    public void shutdown() {
        client.close();
    }

    public RichPresence processHypixel() {
        if (HCC.INSTANCE.getHandlers().getHypixelDetector().isHypixel())
            return new RichPresence(
                    "HCC " + Metadata.getVersion(),
                    "Hypixel: ",
                    OffsetDateTime.now(),
                    null,
                    "16",
                    "Hypixel Network",
                    "compass",
                    "In server",
                    null,
                    0,
                    0,
                    null,
                    null,
                    null,
                    false
            );


        return null;
    }
}
