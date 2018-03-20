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

package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderPlayerEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity<AbstractClientPlayer> {

    public MixinRenderPlayer(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    @Shadow
    public abstract ModelPlayer getMainModel();

    @Inject(method = "doRender", at = @At("HEAD"))
    private void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        EventBus.INSTANCE.post(new RenderPlayerEvent(entity, renderManager, x, y, z, partialTicks));

        //Start DAB
        int dabTicks = Hyperium.INSTANCE.getCosmetics().getDabCosmetic().getDabTicks(entity.getUniqueID());
        if (dabTicks <= -1)
            return;
        //Dabq
        float heldPercent = 100;
        int dabAnimTicks = Hyperium.INSTANCE.getCosmetics().getDabCosmetic().getDabAnimTicks(entity.getUniqueID());
        if (dabAnimTicks > 0) {
            heldPercent = 10 * (10 - dabAnimTicks);
        }
        if (dabTicks < 10)
            heldPercent = 10 * (dabTicks);
        heldPercent /= 100;

        getMainModel().bipedRightArm.rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
        getMainModel().bipedRightArm.rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
        getMainModel().bipedRightArmwear.rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
        getMainModel().bipedRightArmwear.rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
        getMainModel().bipedLeftArm.rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
        getMainModel().bipedLeftArm.rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
        getMainModel().bipedLeftArm.rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);
        getMainModel().bipedLeftArmwear.rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
        getMainModel().bipedLeftArmwear.rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
        getMainModel().bipedLeftArmwear.rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);
        final float rotationX = entity.rotationPitch;
        getMainModel().bipedHead.rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
        final float rotationY = ((EntityPlayer) entity).renderYawOffset - entity.rotationYaw;
        getMainModel().bipedHead.rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);
        getMainModel().bipedHeadwear.rotateAngleX = (float) Math.toRadians(45.0f * heldPercent);
        getMainModel().bipedHeadwear.rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
//            if (isOurPlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
//                heldPercent = (InputEvent.prevDabbingHeld + (InputEvent.dabbingHeld - InputEvent.prevDabbingHeld) * InputEvent.firstPersonPartialTicks) / 8.0f;
//                GlStateManager.rotate(-50.0f * heldPercent, 1.0f, 0.0f, 0.0f);
//                GlStateManager.rotate(30.0f * heldPercent, 0.0f, 1.0f, 0.0f);
//                GlStateManager.rotate(-30.0f * heldPercent, 0.0f, 0.0f, 1.0f);
//                GlStateManager.translate(-0.3 * heldPercent, -0.2 * heldPercent, -0.5 * heldPercent);
//            }

    }


}

