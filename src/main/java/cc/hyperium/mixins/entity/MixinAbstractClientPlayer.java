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
import cc.hyperium.config.Settings;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.SharedMonsterAttributes;
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
        Hyperium.INSTANCE.getHandlers().getCapeHandler().getCape((AbstractClientPlayer)(Object)this);
    }

    /**
     * @author
     */
    @Overwrite
    public ResourceLocation getLocationCape() {

        ResourceLocation cape = Hyperium.INSTANCE.getHandlers().getCapeHandler().getCape((AbstractClientPlayer)(Object)this);
        if (cape != null)
            return cape;
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
    }

    @Inject(method = "getFovModifier", at = @At("HEAD"), cancellable = true)
    private void getFovModifier(CallbackInfoReturnable<Float> ci) {
        if (Settings.STATIC_FOV) {
            if (Minecraft.getMinecraft().thePlayer.isSprinting() && Settings.staticFovSprintModifier)
                ci.setReturnValue((float)(1.0 * ((Minecraft.getMinecraft().thePlayer.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() / (double)Minecraft.getMinecraft().thePlayer.capabilities.getWalkSpeed() + 1.0D) / 2.0D)));
            else
            ci.setReturnValue(1.0F);
        }
    }
}
