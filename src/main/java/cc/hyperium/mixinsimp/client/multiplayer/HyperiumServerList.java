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

package cc.hyperium.mixinsimp.client.multiplayer;

import java.io.File;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.Logger;

public class HyperiumServerList {

    public static void func_147414_b(ServerData p_147414_0_) {
        ServerList serverlist = new ServerList(Minecraft.getMinecraft());
        serverlist.loadServerList();

        for (int i = 0; i < serverlist.countServers(); ++i) {
            ServerData serverdata = serverlist.getServerData(i);

            if (serverdata.serverName.equals(p_147414_0_.serverName) && serverdata.serverIP.equals(p_147414_0_.serverIP)) {
                serverlist.func_147413_a(i, p_147414_0_);
                break;
            }
        }

        serverlist.saveServerList();
    }

    public void loadServers(List<ServerData> servers, Logger logger, Minecraft mc) {
        try {
            servers.clear();
            NBTTagCompound nbttagcompound = CompressedStreamTools
                .read(new File(mc.mcDataDir, "servers.dat"));

            if (nbttagcompound == null) {
                return;
            }

            NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                servers.add(ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)));
            }
        } catch (Exception exception) {
            logger.error("Couldn\'t load server list", exception);
            System.out.println("Load server list error");

        }
    }

    public void saveServerList(List<ServerData> servers, Logger logger, Minecraft mc) {
        try {
            NBTTagList nbttaglist = new NBTTagList();

            for (ServerData serverdata : servers) {
                nbttaglist.appendTag(serverdata.getNBTCompound());
            }

            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("servers", nbttaglist);
            CompressedStreamTools.safeWrite(nbttagcompound, new File(mc.mcDataDir, "servers.dat"));
        } catch (Exception exception) {
            logger.error("Couldn\'t save server list", exception);
            System.out.println("[ServerListMixin] Save server list error");
        }
    }

    public ServerData getServerData(List<ServerData> servers, int p_78850_1_) {
        try {
            return servers.get(p_78850_1_);
        } catch (Exception e) {
            System.out.println("[ServerListMixin] GetServer Data error 1");
            e.printStackTrace();
        }
        return null;
    }

    public void removeServerData(List<ServerData> servers, int p_78851_1_) {
        try {
            servers.remove(p_78851_1_);
        } catch (Exception e) {
            System.out.println("[ServerListMixin] Remove server data error");
        }
    }

    public void addServerData(List<ServerData> servers, ServerData p_78849_1_) {
        try {
            servers.add(p_78849_1_);
        } catch (Exception e) {
            System.out.println("[ServerListMixin] Add server data error");
        }
    }

    public int countServers(List<ServerData> servers) {
        return servers.size();
    }

    public void swapServers(List<ServerData> servers, Logger logger, Minecraft mc, int p_78857_1_, int p_78857_2_) {
        try {
            ServerData serverdata = this.getServerData(servers, p_78857_1_);
            servers.set(p_78857_1_, this.getServerData(servers, p_78857_2_));
            servers.set(p_78857_2_, serverdata);
            this.saveServerList(servers, logger, mc);
        } catch (Exception e) {
            System.out.println("[ServerListMixin] Swap servers error");
        }
    }

    public void func_147413_a(List<ServerData> servers, int p_147413_1_, ServerData p_147413_2_) {
        try {
            servers.set(p_147413_1_, p_147413_2_);
        } catch (Exception e) {
            System.out.println("[HyperiumServerList] func_147413_a server data error");
        }
    }
}
