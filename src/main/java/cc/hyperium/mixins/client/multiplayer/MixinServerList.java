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
    @Shadow
    @Final
    private static Logger logger;
    @Shadow
    @Final
    private List<ServerData> servers;
    @Shadow
    @Final
    private Minecraft mc;

    private HyperiumServerList hyperiumServerList = new HyperiumServerList();

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public static void func_147414_b(ServerData p_147414_0_) {
        HyperiumServerList.func_147414_b(p_147414_0_);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void loadServerList() {
        hyperiumServerList.loadServers(this.servers, logger, this.mc);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void saveServerList() {
        hyperiumServerList.saveServerList(this.servers, logger, this.mc);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public ServerData getServerData(int p_78850_1_) {
        return hyperiumServerList.getServerData(this.servers, p_78850_1_);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void removeServerData(int p_78851_1_) {
        hyperiumServerList.removeServerData(this.servers, p_78851_1_);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void addServerData(ServerData p_78849_1_) {
        hyperiumServerList.addServerData(this.servers, p_78849_1_);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public int countServers() {
        return hyperiumServerList.countServers(this.servers);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void swapServers(int p_78857_1_, int p_78857_2_) {
        hyperiumServerList.swapServers(this.servers, logger, this.mc, p_78857_1_, p_78857_2_);
    }

    /**
     * @author Sk1er
     * @reason Prevent crash from NPE
     */
    @Overwrite
    public void func_147413_a(int p_147413_1_, ServerData p_147413_2_) {
        hyperiumServerList.func_147413_a(this.servers, p_147413_1_, p_147413_2_);
    }
}
