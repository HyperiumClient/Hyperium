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

package cc.hyperium.mixins.client.network;

import cc.hyperium.mods.nickhider.NickHider;
import cc.hyperium.mods.nickhider.config.NickHiderConfig;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetworkPlayerInfo.class)
public abstract class MixinNetworkPlayerInfo {

    @Shadow @Final
    private GameProfile gameProfile;

    @Inject(method = "getLocationSkin", at = @At("HEAD"), cancellable = true)
    private void getLocationSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        NickHider instance = NickHider.instance;

        if (instance != null && instance.getNickHiderConfig().isHideSkins() && instance.getNickHiderConfig().isMasterEnabled()) {
            NickHiderConfig config = instance.getNickHiderConfig();

            if (gameProfile.getId().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
                cir.setReturnValue(config.isUseRealSkinForSelf() && instance.getPlayerSkin() != null ? instance.getPlayerSkin() :
                    DefaultPlayerSkin.getDefaultSkin(gameProfile.getId()));
            } else if (config.isHideOtherSkins()) {
                cir.setReturnValue(config.isUsePlayerSkinForAll() && instance.getPlayerSkin() != null ? instance.getPlayerSkin() :
                    DefaultPlayerSkin.getDefaultSkin(gameProfile.getId()));
            }
        }
    }

    @Inject(method = "getSkinType", at = @At("RETURN"), cancellable = true)
    private void getSkinType(CallbackInfoReturnable<String> type) {
        NickHider instance = NickHider.instance;

        if (instance != null && instance.getNickHiderConfig().isHideSkins() && instance.getNickHiderConfig().isMasterEnabled()) {
            NickHiderConfig config = instance.getNickHiderConfig();

            if (gameProfile.getId().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
                if (config.isUseRealSkinForSelf() && instance.getPlayerSkin() != null) {
                    type.setReturnValue(instance.getPlayerRealSkinType());
                }
            } else if (config.isHideOtherSkins()) {
                if (config.isUsePlayerSkinForAll() && instance.getPlayerSkin() != null) {
                    type.setReturnValue(instance.getPlayerRealSkinType());
                }
            }
        }
    }

    @Inject(method = "getLocationCape", at = @At("RETURN"), cancellable = true)
    private void getLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
        NickHider instance = NickHider.instance;

        if (cir.getReturnValue() != null) return;

        if (instance != null && instance.getNickHiderConfig().isHideSkins() && instance.getNickHiderConfig().isMasterEnabled()) {
            NickHiderConfig config = instance.getNickHiderConfig();

            if (gameProfile.getId().equals(Minecraft.getMinecraft().thePlayer.getUniqueID()) && config.isUseRealSkinForSelf()) {
                cir.setReturnValue(instance.getPlayerCape());
            }
        }
    }
}
