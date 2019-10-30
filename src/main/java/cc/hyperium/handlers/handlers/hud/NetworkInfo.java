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

package cc.hyperium.handlers.handlers.hud;


import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.ServerJoinEvent;
import cc.hyperium.event.network.server.ServerLeaveEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.Collection;

public class NetworkInfo {
    private static NetworkInfo instance;
    private ServerAddress currentServerAddress;
    private Minecraft mc;

    public NetworkInfo() {
        mc = Minecraft.getMinecraft();
        NetworkInfo.instance = this;
    }

    public static NetworkInfo getInstance() {
        return NetworkInfo.instance;
    }

    @InvokeEvent
    public void onServerJoin(ServerJoinEvent event) {
        currentServerAddress = ServerAddress.fromString(event.getServer());
    }

    @InvokeEvent
    public void onServerLeave(ServerLeaveEvent event) {
        currentServerAddress = null;
    }

    public ServerAddress getCurrentServerAddress() {
        return currentServerAddress;
    }

    private NetworkPlayerInfo getPlayerInfo(final String ign) {
        Collection<NetworkPlayerInfo> map = mc.getNetHandler().getPlayerInfoMap();
        return map.stream().filter(playerInfo -> playerInfo.getGameProfile().getName().equalsIgnoreCase(ign)).findFirst().orElse(null);
    }

    public int getPing(final String ign) {
        NetworkPlayerInfo networkInfo = getPlayerInfo(ign);
        return networkInfo == null ? -1 : networkInfo.getResponseTime();
    }

    public void printPing(final String name) {
        NetworkPlayerInfo info = getPlayerInfo(name);
        GeneralChatHandler.instance().sendMessage(info == null || info.getResponseTime() < 0 ? ChatColor.RED + "No info about " + name :
            ChatColor.WHITE.toString() + ChatColor.BOLD + info.getGameProfile().getName() + ChatColor.WHITE + ": " + info.getResponseTime() + "ms");
    }
}
