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

package cc.hyperium.mixinsimp.client.multiplayer;

import cc.hyperium.Hyperium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class HyperiumServerList {

    public static void saveSingleServer(ServerData serverData) {
        ServerList serverlist = new ServerList(Minecraft.getMinecraft());
        serverlist.loadServerList();

        for (int i = 0; i < serverlist.countServers(); ++i) {
            ServerData serverdata = serverlist.getServerData(i);

            if (serverdata.serverName.equals(serverData.serverName) && serverdata.serverIP.equals(serverData.serverIP)) {
                serverlist.func_147413_a(i, serverData);
                break;
            }
        }

        serverlist.saveServerList();
    }

    public void loadServerList(List<ServerData> servers, Minecraft mc) {
        try {
            servers.clear();
            NBTTagCompound nbttagcompound = CompressedStreamTools
                .read(new File(mc.mcDataDir, "servers.dat"));

            if (nbttagcompound == null) return;

            NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);

            int bound = nbttaglist.tagCount();
            for (int i = 0; i < bound; i++) {
                ServerData serverDataFromNBTCompound = ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i));
                servers.add(serverDataFromNBTCompound);
            }
        } catch (Exception exception) {
            Hyperium.LOGGER.error("Failed to load server list", exception);
        }
    }

    public void saveServerList(List<ServerData> servers, Minecraft mc) {
        try {
            NBTTagList nbttaglist = new NBTTagList();

            for (ServerData server : servers) {
                NBTTagCompound nbtCompound = server.getNBTCompound();
                nbttaglist.appendTag(nbtCompound);
            }

            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("servers", nbttaglist);
            CompressedStreamTools.safeWrite(nbttagcompound, new File(mc.mcDataDir, "servers.dat"));
        } catch (Exception exception) {
            Hyperium.LOGGER.error("Save server list error", exception);
        }
    }

    public ServerData getServerData(List<ServerData> servers, int index) {
        try {
            return servers.get(index);
        } catch (Exception e) {
            Hyperium.LOGGER.error("Failed to get server data", e);
        }

        return null;
    }

    public void removeServerData(List<ServerData> servers, int index) {
        try {
            servers.remove(index);
        } catch (Exception e) {
            Hyperium.LOGGER.error("Failed to remove server data", e);
        }
    }

    public void addServerData(List<ServerData> servers, ServerData index) {
        try {
            servers.add(index);
        } catch (Exception e) {
            Hyperium.LOGGER.error("Failed to add server data", e);
        }
    }

    public int countServers(List<ServerData> servers) {
        return servers.size();
    }

    public void swapServers(List<ServerData> servers, Minecraft mc, int pos1, int pos2) {
        try {
            ServerData serverdata = getServerData(servers, pos1);
            servers.set(pos1, getServerData(servers, pos2));
            servers.set(pos2, serverdata);
            saveServerList(servers, mc);
        } catch (Exception e) {
            Hyperium.LOGGER.error("Failed to swap servers", e);
        }
    }

    public void set(List<ServerData> servers, int index, ServerData server) {
        try {
            servers.set(index, server);
        } catch (Exception e) {
            Hyperium.LOGGER.error("Failed to set server data", e);
        }
    }
}
