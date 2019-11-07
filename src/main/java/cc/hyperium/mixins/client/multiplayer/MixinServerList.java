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

package cc.hyperium.mixins.client.multiplayer;

import cc.hyperium.mixinsimp.client.multiplayer.HyperiumServerList;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerList.class)
public class MixinServerList {

    @Shadow @Final private static Logger logger;
    @Shadow @Final private List<ServerData> servers;
    @Shadow @Final private Minecraft mc;

    private HyperiumServerList hyperiumServerList = new HyperiumServerList();

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public static void func_147414_b(ServerData serverData) {
        HyperiumServerList.saveSingleServer(serverData);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void loadServerList() {
        hyperiumServerList.loadServerList(servers, mc);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void saveServerList() {
        hyperiumServerList.saveServerList(servers, mc);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public ServerData getServerData(int index) {
        return hyperiumServerList.getServerData(servers, index);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void removeServerData(int index) {
        hyperiumServerList.removeServerData(servers, index);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void addServerData(ServerData index) {
        hyperiumServerList.addServerData(servers, index);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public int countServers() {
        return hyperiumServerList.countServers(servers);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void swapServers(int pos1, int pos2) {
        hyperiumServerList.swapServers(servers, mc, pos1, pos2);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void func_147413_a(int index, ServerData server) {
        hyperiumServerList.set(servers, index, server);
    }
}
