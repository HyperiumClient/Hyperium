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
import com.hcc.event.EventBus;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;

public class RichPresenceManager {

    private IPCClient client;
    private boolean connected = false;

    public void init() {
        client = new IPCClient(412963310867054602L);
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                EventBus.INSTANCE.register(new RichPresenceUpdater(client));
            }
        });
        try {
            client.connect(DiscordBuild.ANY);
            connected = true;
        } catch (NoDiscordClientException e) {
            HCC.logger.warn("no discord clients found");
        }
    }

    public void shutdown() {
        if (connected) {
            client.close();
        }
    }

}
