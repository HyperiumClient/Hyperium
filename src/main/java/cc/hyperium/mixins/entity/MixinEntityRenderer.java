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
import cc.hyperium.event.DrawBlockHighlightEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.mods.common.PerspectiveModifierContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Shadow
    private float thirdPersonDistance;
    @Shadow
    private float thirdPersonDistanceTemp;
    @Shadow
    private boolean cloudFog;
    @Shadow
    private boolean debugView;
    @Shadow
    private Minecraft mc;
    @Shadow
    private float fovModifierHandPrev;
    @Shadow
    private float fovModifierHand;
    @Shadow
    private MouseFilter mouseFilterYAxis;
    @Shadow
    private MouseFilter mouseFilterXAxis;
    @Shadow
    private boolean drawBlockOutline;
    private boolean zoomMode = false;

    //endStartSection
    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void updateCameraAndRender(float partialTicks, long nano, CallbackInfo ci) {
        EventBus.INSTANCE.post(new RenderEvent());
    }

    /**
     * @author CoalOres
     * @reason 360 Perspective
     */
    @Overwrite
    private void orientCamera(final float partialTicks) {
        final Entity entity = this.mc.getRenderViewEntity();
        float f = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        double d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
        if (Hyperium.INSTANCE.getHandlers().getConfigOptions().turnPeopleIntoBlock)
            d2 -= 1.0;
        double d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;

        if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPlayerSleeping()) {
            ++f;
            GlStateManager.translate(0.0f, 0.3f, 0.0f);
            if (!this.mc.gameSettings.debugCamEnable) {
                final BlockPos blockpos = new BlockPos(entity);
                final IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (block == Blocks.bed) {
                    int j = iblockstate.getValue(BlockBed.FACING).getHorizontalIndex();
                    GlStateManager.rotate((float) (j * 90), 0.0F, 1.0F, 0.0F);
                }
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, -1.0f, 0.0f);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0f, 0.0f, 0.0f);
            }
        } else if (this.mc.gameSettings.thirdPersonView > 0) {
            double d4 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks;
            if (this.mc.gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0f, 0.0f, (float) (-d4));
            } else {
                float f2 = entity.rotationYaw;
                float f3 = entity.rotationPitch;
                if (PerspectiveModifierContainer.enabled) {
                    f2 = PerspectiveModifierContainer.modifiedYaw;
                    f3 = PerspectiveModifierContainer.modifiedPitch;
                }
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    f3 += 180.0f;
                }
                final double d5 = -MathHelper.sin(f2 / 180.0f * 3.1415927f) * MathHelper.cos(f3 / 180.0f * 3.1415927f) * d4;
                final double d6 = MathHelper.cos(f2 / 180.0f * 3.1415927f) * MathHelper.cos(f3 / 180.0f * 3.1415927f) * d4;
                final double d7 = -MathHelper.sin(f3 / 180.0f * 3.1415927f) * d4;
                for (int i = 0; i < 8; ++i) {
                    float f4 = (i & 0x1) * 2 - 1;
                    float f5 = (i >> 1 & 0x1) * 2 - 1;
                    float f6 = (i >> 2 & 0x1) * 2 - 1;
                    f4 *= 0.1f;
                    f5 *= 0.1f;
                    f6 *= 0.1f;
                    final MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(d0 + f4, d2 + f5, d3 + f6), new Vec3(d0 - d5 + f4 + f6, d2 - d7 + f5, d3 - d6 + f6));
                    if (movingobjectposition != null) {
                        final double d8 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d2, d3));
                        if (d8 < d4) {
                            d4 = d8;
                        }
                    }
                }
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (PerspectiveModifierContainer.enabled) {
                    GlStateManager.rotate(PerspectiveModifierContainer.modifiedPitch - f3, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(PerspectiveModifierContainer.modifiedYaw - f2, 0.0f, 1.0f, 0.0f);
                    GlStateManager.translate(0.0f, 0.0f, (float) (-d4));
                    GlStateManager.rotate(f2 - PerspectiveModifierContainer.modifiedYaw, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(f3 - PerspectiveModifierContainer.modifiedPitch, 1.0f, 0.0f, 0.0f);
                } else {
                    GlStateManager.rotate(entity.rotationPitch - f3, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(entity.rotationYaw - f2, 0.0f, 1.0f, 0.0f);
                    GlStateManager.translate(0.0f, 0.0f, (float) (-d4));
                    GlStateManager.rotate(f2 - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(f3 - entity.rotationPitch, 1.0f, 0.0f, 0.0f);
                }
            }
        } else {
            GlStateManager.translate(0.0f, 0.0f, -0.1f);
        }
        if (!this.mc.gameSettings.debugCamEnable) {
            float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f;
            final float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            final float roll = 0.0f;
            if (entity instanceof EntityAnimal) {
                final EntityAnimal entityanimal = (EntityAnimal) entity;
                yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0f;
            }
            final Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
            if (PerspectiveModifierContainer.enabled) {
                GlStateManager.rotate(roll, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(PerspectiveModifierContainer.modifiedPitch, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(PerspectiveModifierContainer.modifiedYaw + 180.0f, 0.0f, 1.0f, 0.0f);
            } else {
                GlStateManager.rotate(roll, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(pitch, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(yaw, 0.0f, 1.0f, 0.0f);
            }
        }
        GlStateManager.translate(0.0f, -f, 0.0f);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
        d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d2, d3, partialTicks);
        if (Hyperium.INSTANCE.getHandlers().getConfigOptions().turnPeopleIntoBlock) {
            GlStateManager.translate(0.0F, 1.0F, 0.0F);
        }
    }

    /**
     * @author CoalOres
     * @reason 360 Perspective
     */

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=outline"), cancellable = true)
    public void drawOutline(int pass, float part, long nano, CallbackInfo info) {
        DrawBlockHighlightEvent drawBlockHighlightEvent = new DrawBlockHighlightEvent(((EntityPlayer) this.mc.getRenderViewEntity()), mc.objectMouseOver, part);
        EventBus.INSTANCE.post(drawBlockHighlightEvent);
        if (drawBlockHighlightEvent.isCancelled()) {
            Hyperium.INSTANCE.getHandlers().getConfigOptions().isCancelBox = true;

        }
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", args = "ldc=mouse"))
    private void updateCameraAndRender2(float partialTicks, long nanoTime, CallbackInfo ci) {
        boolean flag2 = Display.isActive();
        if (Minecraft.getMinecraft().inGameHasFocus && flag2) {
            if (PerspectiveModifierContainer.enabled && Minecraft.getMinecraft().gameSettings.thirdPersonView != 1) {
                PerspectiveModifierContainer.onDisable();
                PerspectiveModifierContainer.setEnabled(false);
            }

            if (PerspectiveModifierContainer.enabled) {
                Minecraft.getMinecraft().mouseHelper.mouseXYChange();

                float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
                float f1 = f * f * f * 8.0F;
                float f2 = (float) Minecraft.getMinecraft().mouseHelper.deltaX * f1;
                float f3 = (float) Minecraft.getMinecraft().mouseHelper.deltaY * f1;

                // Modifying pitch and yaw values.
                PerspectiveModifierContainer.modifiedYaw += f2 / 8.0F;
                PerspectiveModifierContainer.modifiedPitch += f3 / 8.0F;

                // Checking if pitch exceeds maximum range.
                if (Math.abs(PerspectiveModifierContainer.modifiedPitch) > 90.0F) {
                    if (PerspectiveModifierContainer.modifiedPitch > 0.0F) {
                        PerspectiveModifierContainer.modifiedPitch = 90.0F;
                    } else {
                        PerspectiveModifierContainer.modifiedPitch = -90.0F;
                    }
                }
            }
        }
    }
}