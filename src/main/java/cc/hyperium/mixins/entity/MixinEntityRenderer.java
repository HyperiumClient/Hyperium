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
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.Vec3;
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
    private boolean zoomMode = false;

    private float distanceModifier = 0.0f;

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V", shift = At.Shift.AFTER))
    private void updateCameraAndRender(float partialTicks, long nano, CallbackInfo ci) {
        EventBus.INSTANCE.post(new RenderEvent());
    }

    /**
     * @author CoalOres
     * @reason 360 Perspective
     */
   /* @Overwrite
    private void orientCamera(float partialTicks) {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        float f = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;

        if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPlayerSleeping()) {
            f = (float) ((double) f + 1.0D);
            GlStateManager.translate(0.0F, 0.3F, 0.0F);

            if (!Minecraft.getMinecraft().gameSettings.debugCamEnable) {
                BlockPos blockpos = new BlockPos(entity);
                IBlockState iblockstate = Minecraft.getMinecraft().theWorld.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (block == Blocks.bed) {
                    int j = iblockstate.getValue(BlockBed.FACING).getHorizontalIndex();
                    GlStateManager.rotate((float) (j * 90), 0.0F, 1.0F, 0.0F);
                }

                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
            }
        } else if (Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
            // Ray tracing attempts.

            // Gets the current position of the camera.
            Vec3 camPos = ActiveRenderInfo.getPosition();

            // This is actually the position relative to the player, so we have to adjust for that.
            Vec3 playerPos = Minecraft.getMinecraft().thePlayer.getPositionVector();

            Vec3 trueCamPos = camPos.add(playerPos);

            double d3 = (double) (this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks);
            MovingObjectPosition mop = Minecraft.getMinecraft().theWorld.rayTraceBlocks(trueCamPos,playerPos);
            if(mop != null && mop.entityHit != null){
                System.out.println("Entity looked at: " + mop.entityHit.getName());
            }
            System.out.println(mop.hitVec);

            // Now we need to check if there's a block in the camera, if so, stop it.
            if(isBlockAtPos(trueCamPos)){
                // There's a block in the camera!
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("There's a block in the camera!");

                // Stop it!

                double reduction = 1;

                d3 = (double) (this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks) -reduction;
            }

            if (Minecraft.getMinecraft().gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
            } else {
                float f1 = entity.rotationYaw;
                float f2 = entity.rotationPitch;


                if (Hyperium.INSTANCE.getPerspective().isEnabled()) {
                    f1 = Hyperium.INSTANCE.getPerspective().modifiedYaw;
                    f2 = Hyperium.INSTANCE.getPerspective().modifiedPitch;
                }

                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
                    f2 += 180.0F;
                }

                double d4 = (double) (-MathHelper.sin(f1 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d3;
                double d5 = (double) (MathHelper.cos(f1 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d3;
                double d6 = (double) (-MathHelper.sin(f2 / 180.0F * (float) Math.PI)) * d3;

                for (int i = 0; i < 8; ++i) {
                    float f3 = (float) ((i & 1) * 2 - 1);
                    float f4 = (float) ((i >> 1 & 1) * 2 - 1);
                    float f5 = (float) ((i >> 2 & 1) * 2 - 1);
                    f3 = f3 * 0.1F;
                    f4 = f4 * 0.1F;
                    f5 = f5 * 0.1F;
                    MovingObjectPosition movingobjectposition = Minecraft.getMinecraft().theWorld.rayTraceBlocks(new Vec3(d0 + (double) f3, d1 + (double) f4, d2 + (double) f5), new Vec3(d0 - d4 + (double) f3 + (double) f5, d1 - d6 + (double) f4, d2 - d5 + (double) f5));

                    if (movingobjectposition != null) {
                        double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));

                        if (d7 < d3) {
                            d3 = d7;
                        }
                    }
                }

                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                // Replaced with modified yaw and pitch values.

                if (Hyperium.INSTANCE.getPerspective().isEnabled()) {
                    GlStateManager.rotate(Hyperium.INSTANCE.getPerspective().modifiedPitch - f2, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(Hyperium.INSTANCE.getPerspective().modifiedYaw - f1, 0.0F, 1.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
                    GlStateManager.rotate(f1 - Hyperium.INSTANCE.getPerspective().modifiedYaw, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f2 - Hyperium.INSTANCE.getPerspective().modifiedPitch, 1.0F, 0.0F, 0.0F);
                } else {
                    GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
                    GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
                }
            }
        } else {
            GlStateManager.translate(0.0F, 0.0F, -0.1F);
        }

        if (!Minecraft.getMinecraft().gameSettings.debugCamEnable) {
            // Further modifications.
            if (Hyperium.INSTANCE.getPerspective().isEnabled()) {
                GlStateManager.rotate(Hyperium.INSTANCE.getPerspective().modifiedPitch, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(Hyperium.INSTANCE.getPerspective().modifiedYaw + 180.0F, 0.0F, 1.0F, 0.0F);
            } else {
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
            }

            if (entity instanceof EntityAnimal) {
                EntityAnimal entityanimal = (EntityAnimal) entity;
                GlStateManager.rotate(entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
            } else {
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
            }
        }

        GlStateManager.translate(0.0F, -f, 0.0F);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
        d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
        d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
        this.cloudFog = Minecraft.getMinecraft().renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
    }*/

    private boolean isBlockAtPos(Vec3 blockPos){
        Block blockPresent = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(blockPos.xCoord,blockPos.yCoord,blockPos.zCoord)).getBlock();
        if(blockPresent != null && !blockPresent.getLocalizedName().contains("air")){
            return true;
        } else{
            return false;
        }

    }

    /**
     * @author CoalOres
     * @reason 360 Perspective
     */

    /*@Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", args = "ldc=mouse"))
    private void updateCameraAndRender2(float partialTicks, long nanoTime, CallbackInfo ci) {
        boolean flag2 = Display.isActive();
        if (Minecraft.getMinecraft().inGameHasFocus && flag2) {
            if (Hyperium.INSTANCE.getPerspective().isEnabled() && Minecraft.getMinecraft().gameSettings.thirdPersonView != 2) {
                Hyperium.INSTANCE.getPerspective().onDisable();
                Hyperium.INSTANCE.getPerspective().setEnabled(false);
            }

            if (Hyperium.INSTANCE.getPerspective().isEnabled()) {
                Minecraft.getMinecraft().mouseHelper.mouseXYChange();

                float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
                float f1 = f * f * f * 8.0F;
                float f2 = (float) Minecraft.getMinecraft().mouseHelper.deltaX * f1;
                float f3 = (float) Minecraft.getMinecraft().mouseHelper.deltaY * f1;

                // Modifying pitch and yaw values.
                Hyperium.INSTANCE.getPerspective().modifiedYaw += f2 / 8.0F;
                Hyperium.INSTANCE.getPerspective().modifiedPitch += f3 / 8.0F;

                // Checking if pitch exceeds maximum range.
                if (Math.abs(Hyperium.INSTANCE.getPerspective().modifiedPitch) > 90.0F) {
                    if (Hyperium.INSTANCE.getPerspective().modifiedPitch > 0.0F) {
                        Hyperium.INSTANCE.getPerspective().modifiedPitch = 90.0F;
                    } else {
                        Hyperium.INSTANCE.getPerspective().modifiedPitch = -90.0F;
                    }
                }
            }
        }
    }*/

    /**
     * Camera zooming
     *
     * @author boomboompower
     */
    @Overwrite
    private float getFOVModifier(float partialTicks, boolean notHand) {
        if (this.debugView) {
            return 90.0F;
        } else {
            Entity entity = this.mc.getRenderViewEntity();
            float fov = 70.0F;

            if (notHand) {
                fov = this.mc.gameSettings.fovSetting * (this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks);
            }

            boolean flag = false;

            if (this.mc.currentScreen == null) {
                GameSettings gamesettings = this.mc.gameSettings;
                flag = GameSettings.isKeyDown(((cc.hyperium.mods.utilities.UtilitiesMod) Hyperium.INSTANCE.getModIntegration().getUtilities()).getBinding());
            }

            if (flag) {
                if (!this.zoomMode) {
                    this.zoomMode = true;
                    this.mc.gameSettings.smoothCamera = true;
                }

                fov /= 4.0F;
            } else if (this.zoomMode) {
                this.zoomMode = false;
                this.mc.gameSettings.smoothCamera = false;
                this.mouseFilterXAxis = new MouseFilter();
                this.mouseFilterYAxis = new MouseFilter();
                this.mc.renderGlobal.setDisplayListEntitiesDirty();
            }

            if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() <= 0.0F) {
                float f1 = (float) ((EntityLivingBase) entity).deathTime + partialTicks;
                fov /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
            }

            Block block = ActiveRenderInfo
                    .getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);

            if (block.getMaterial() == Material.water) {
                fov = fov * 60.0F / 70.0F;
            }

            return fov;
        }
    }
}