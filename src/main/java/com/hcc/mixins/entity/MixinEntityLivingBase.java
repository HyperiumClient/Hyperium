package com.hcc.mixins.entity;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends MixinEntity {

    /**
     * MouseDelayFix
     *
     * @author prplz
     */
    @Inject(method = "getLook", at = @At("HEAD"), cancellable = true)
    private void getLook(float partialTicks, CallbackInfoReturnable<Vec3> ci) {
        EntityLivingBase base = (EntityLivingBase) (Object) this;
        if (base instanceof EntityPlayerSP) {
            ci.setReturnValue(super.getLook(partialTicks));
        }
    }


}