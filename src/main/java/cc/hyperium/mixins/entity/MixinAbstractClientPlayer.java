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

import cc.hyperium.mixinsimp.client.entity.HyperiumAbstractClientPlayer;
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

    private HyperiumAbstractClientPlayer hyperiumAbstractClientPlayer = new HyperiumAbstractClientPlayer((AbstractClientPlayer) (Object) this);

    @Shadow
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void AbstractPlayer(World worldIn, GameProfile playerProfile, CallbackInfo callbackInfo) {

        //Blank get cape call to get the show on the road
        hyperiumAbstractClientPlayer.init();
    }

    /**
     * @author
     */
    @Overwrite
    public ResourceLocation getLocationCape() {
        return hyperiumAbstractClientPlayer.getLocationCape();
    }

    @Inject(method = "getFovModifier", at = @At("HEAD"), cancellable = true)
    private void getFovModifier(CallbackInfoReturnable<Float> ci) {
        hyperiumAbstractClientPlayer.getFovModifier(ci);
    }
}
