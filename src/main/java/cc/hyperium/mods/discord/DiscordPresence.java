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
import cc.hyperium.event.EventBus;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;

public class DiscordPresence {

    private IPCClient client = new IPCClient(412963310867054602L);

    public void load() {
        if (Settings.DISCORD_RP) {
            client.setListener(new IPCListener() {
                @Override
                public void onReady(IPCClient client) {
                    EventBus.INSTANCE.register(new RPCUpdater(client));
                }
            });

            try {
                client.connect(DiscordBuild.ANY);
            } catch (NoDiscordClientException | RuntimeException e) {
                Hyperium.LOGGER.warn("No discord client found.");
            }
        }
    }

    public void shutdown() {
        try {
            if (client != null && client.getStatus() == PipeStatus.CONNECTED) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
