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

import cc.hyperium.Hyperium;
import cc.hyperium.gui.settings.items.GeneralSetting;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {


    @Shadow
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void AbstractPlayer(World worldIn, GameProfile playerProfile, CallbackInfo callbackInfo) {

        //Blank get cape call to get the show on the road
        Hyperium.INSTANCE.getHandlers().getCapeHandler().getCape(playerProfile.getId());
    }

    /**
     * @author
     */
    @Overwrite
    public ResourceLocation getLocationCape() {
        if (Hyperium.INSTANCE.getHandlers().getCapeHandler().getCape(getPlayerInfo().getGameProfile().getId()) == null) {
            Hyperium.INSTANCE.getHandlers().getCapeHandler().loadCape(getPlayerInfo().getGameProfile().getId(), "http://s.optifine.net/capes/" + getPlayerInfo().getGameProfile().getName() + ".png");
            ResourceLocation rl = Hyperium.INSTANCE.getHandlers().getCapeHandler().getCape(getPlayerInfo().getGameProfile().getId());
            return rl;
        }
        ResourceLocation cape = Hyperium.INSTANCE.getHandlers().getCapeHandler().getCape(getPlayerInfo().getGameProfile().getId());
        if (cape != null)
            return cape;
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
    }

    @Inject(method = "getFovModifier", at = @At("HEAD"), cancellable = true)
    private void getFovModifier(CallbackInfoReturnable<Float> ci) {
        if (GeneralSetting.staticFovEnabled) {
            ci.setReturnValue(1.0F);
        }
    }
}
