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

package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import club.sk1er.website.api.requests.HypixelApiGuild;
import club.sk1er.website.api.requests.HypixelApiPlayer;

public class StatsHandler {

    public void initStatsViewer(String player) {
        Multithreading.runAsync(() -> {
            GeneralChatHandler.instance().sendMessage("Loading stats for: " + player);
            try {
                HypixelApiPlayer apiPlayer = HypixelAPI.INSTANCE.getPlayer(player).get();

                new PlayerStatsGui(apiPlayer).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadGuildByPlayer(String player) {
        Multithreading.runAsync(() -> {
            GeneralChatHandler.instance().sendMessage("Loading guild for: " + player);
            try {
                HypixelApiGuild apiGuild = HypixelAPI.INSTANCE.getGuildFromPlayer(player).get();

                new GuildStatsGui(apiGuild).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadGuildByName(String name) {
        Multithreading.runAsync(() -> {
            GeneralChatHandler.instance().sendMessage("Loading guild by name: " + name);
            try {
                HypixelApiGuild apiGuild = HypixelAPI.INSTANCE.getGuildFromName(name).get();

                new GuildStatsGui(apiGuild).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
