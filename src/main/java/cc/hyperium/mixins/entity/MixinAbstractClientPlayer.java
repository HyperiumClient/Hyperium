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

import cc.hyperium.mixinsimp.entity.HyperiumAbstractClientPlayer;
import cc.hyperium.mods.nickhider.NickHider;
import cc.hyperium.mods.nickhider.config.NickHiderConfig;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer {

    private HyperiumAbstractClientPlayer hyperiumAbstractClientPlayer = new HyperiumAbstractClientPlayer((AbstractClientPlayer) (Object) this);

    public MixinAbstractClientPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void AbstractPlayer(World worldIn, GameProfile playerProfile, CallbackInfo callbackInfo) {

        //Blank get cape call to get the show on the road
        hyperiumAbstractClientPlayer.init();
    }

    /**
     * @author - Kevin & Sk1er
     * @reason - Custom Cape Support
     */
    @Overwrite
    public ResourceLocation getLocationCape() {
        return hyperiumAbstractClientPlayer.getLocationCape();
    }

    @Inject(method = "getFovModifier", at = @At("HEAD"), cancellable = true)
    private void getFovModifier(CallbackInfoReturnable<Float> ci) {
        hyperiumAbstractClientPlayer.getFovModifier(ci);
    }


    @Inject(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private void getLocationSkin(CallbackInfoReturnable<ResourceLocation> locationCallbackInfoReturnable) {
        NickHider instance = NickHider.instance;
        if (instance != null && instance.getNickHiderConfig().isHideOtherNames() && instance.getNickHiderConfig().isMasterEnabled()) {
            NickHiderConfig config = instance.getNickHiderConfig();

            if (getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
                locationCallbackInfoReturnable.setReturnValue(config.isUseRealSkinForSelf() && instance.getPlayerSkin() != null
                    ? instance.getPlayerSkin() : DefaultPlayerSkin.getDefaultSkin(getUniqueID()));
            } else {
                if (config.isHideOtherSkins()) {
                    locationCallbackInfoReturnable.setReturnValue(config.isUsePlayerSkinForAll() && instance.getPlayerSkin() != null
                        ? instance.getPlayerSkin() : DefaultPlayerSkin.getDefaultSkin(getUniqueID()));
                }
            }
        }
    }

    @Inject(method = "getSkinType", at = @At("RETURN"), cancellable = true)
    private void getSkinType(CallbackInfoReturnable<String> type) {
        NickHider instance = NickHider.instance;
        if (instance != null && instance.getNickHiderConfig().isHideSkins() && instance.getNickHiderConfig().isMasterEnabled()) {
            NickHiderConfig config = instance.getNickHiderConfig();
            if (getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
                if (config.isUseRealSkinForSelf() && instance.getPlayerSkin() != null) {
                    type.setReturnValue(instance.getPlayerRealSkinType());
                }
            } else {
                if (config.isHideOtherSkins()) {
                    if (config.isUsePlayerSkinForAll() && instance.getPlayerSkin() != null) {
                        type.setReturnValue(instance.getPlayerRealSkinType());
                    }
                }
            }
        }
    }

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    private void getLocationCape(CallbackInfoReturnable<ResourceLocation> locationCallbackInfoReturnable) {
        NickHider instance = NickHider.instance;
        if (locationCallbackInfoReturnable.getReturnValue() != null) {
            return;
        }

        if (instance != null && instance.getNickHiderConfig().isHideSkins() && instance.getNickHiderConfig().isMasterEnabled()) {
            NickHiderConfig config = instance.getNickHiderConfig();

            if (getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
                if (config.isUseRealSkinForSelf()) {
                    locationCallbackInfoReturnable.setReturnValue(instance.getPlayerCape());
                }
            }
        }
    }
}
