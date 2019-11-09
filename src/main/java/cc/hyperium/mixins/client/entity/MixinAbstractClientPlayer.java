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

package cc.hyperium.mixins.client.entity;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.entity.FovUpdateEvent;
import cc.hyperium.handlers.handlers.animation.cape.HyperiumCapeHandler;
import cc.hyperium.mods.nickhider.NickHider;
import cc.hyperium.mods.nickhider.config.NickHiderConfig;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer {

    private HyperiumCapeHandler hook;

    public MixinAbstractClientPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World worldIn, GameProfile playerProfile, CallbackInfo callbackInfo) {
        hook = new HyperiumCapeHandler(playerProfile);
    }

    /**
     * @author - Kevin & Sk1er
     * @reason - Custom Cape Support
     */
    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    private void getHyperiumCape(CallbackInfoReturnable<ResourceLocation> cir) {
        if (hook.getLocationCape() != null) {
            cir.setReturnValue(hook.getLocationCape());
        }
    }

    @Inject(method = "getFovModifier", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void getFovModifier(CallbackInfoReturnable<Float> cir, float f, IAttributeInstance iattributeinstance) {
        FovUpdateEvent event = new FovUpdateEvent((AbstractClientPlayer) (Object) this, f);
        EventBus.INSTANCE.post(event);
        cir.setReturnValue(event.getNewFov());
    }


    @Inject(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private void getLocationSkin(CallbackInfoReturnable<ResourceLocation> locationCallbackInfoReturnable) {
        NickHider instance = NickHider.instance;
        if (instance != null && instance.getNickHiderConfig().isHideSkins() && instance.getNickHiderConfig().isMasterEnabled()) {
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
            } else if (config.isHideOtherSkins() && config.isUsePlayerSkinForAll() && instance.getPlayerSkin() != null) {
                type.setReturnValue(instance.getPlayerRealSkinType());
            }
        }
    }

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    private void getLocationCape(CallbackInfoReturnable<ResourceLocation> locationCallbackInfoReturnable) {
        NickHider instance = NickHider.instance;
        if (locationCallbackInfoReturnable.getReturnValue() != null) return;

        if (instance != null && instance.getNickHiderConfig().isHideSkins() && instance.getNickHiderConfig().isMasterEnabled()) {
            NickHiderConfig config = instance.getNickHiderConfig();

            if (getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID()) && config.isUseRealSkinForSelf()) {
                locationCallbackInfoReturnable.setReturnValue(instance.getPlayerCape());
            }
        }
    }
}
