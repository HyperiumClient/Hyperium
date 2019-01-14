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

package cc.hyperium.mixins.entity;

import cc.hyperium.mixinsimp.entity.HyperiumNetworkPlayerInfo;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetworkPlayerInfo.class)
public abstract class MixinNetworkPlayerInfo {

    @Shadow
    @Final
    private GameProfile gameProfile;

    @Shadow
    private ResourceLocation locationSkin;
    @Shadow
    private ResourceLocation locationCape;

    private HyperiumNetworkPlayerInfo hyperiumNetworkPlayerInfo = new HyperiumNetworkPlayerInfo((NetworkPlayerInfo) (Object) this);

    /**
     * @author - boomboompower
     * @reason - Allow for better cape customization
     */
    @Overwrite
    public ResourceLocation getLocationCape() {
        return hyperiumNetworkPlayerInfo.getLocationCape(this.gameProfile, this.locationCape);
    }

    /**
     *
     * @author - boomboompower
     * @reason - Allow for better skin customization
     */
    @Overwrite
    public ResourceLocation getLocationSkin() {
        return hyperiumNetworkPlayerInfo.getLocationSkin(gameProfile, locationSkin);
    }
}
