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

import cc.hyperium.event.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Final;
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
    protected abstract void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass);

    @Shadow
    private boolean renderHand;

    @Shadow
    protected abstract void renderHand(float partialTicks, int xOffset);

    @Shadow
    protected abstract void renderWorldDirections(float partialTicks);

    @Shadow
    protected abstract void renderRainSnow(float partialTicks);

    @Shadow
    protected abstract void setupFog(int p_78468_1_, float partialTicks);

    @Shadow
    public abstract void disableLightmap();

    @Shadow
    public abstract void enableLightmap();

    @Shadow
    private int frameCount;

    @Shadow
    protected abstract float getFOVModifier(float partialTicks, boolean p_78481_2_);

    @Shadow
    protected abstract void setupCameraTransform(float partialTicks, int pass);

    @Shadow
    protected abstract void updateFogColor(float partialTicks);

    @Shadow
    protected abstract boolean isDrawBlockOutline();

    @Shadow
    private float farPlaneDistance;
    @Shadow
    private Entity pointedEntity;
    @Shadow
    @Final
    public ItemRenderer itemRenderer;
    private float distanceModifier = 0.0f;

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V", shift = At.Shift.AFTER))
    private void renderGui(float partialTicks, long nano, CallbackInfo ci) {
        EventBus.INSTANCE.post(new RenderGuiEvent());
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderWorld(FJ)V", shift = At.Shift.AFTER))
    private void renderWorld(float partialTicks, long nano, CallbackInfo ci) {
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
    private boolean isBlockAtPos(Vec3 blockPos) {
        Block blockPresent = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(blockPos.xCoord, blockPos.yCoord, blockPos.zCoord)).getBlock();
        if (blockPresent != null && !blockPresent.getLocalizedName().contains("air")) {
            return true;
        } else {
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

   /* /**
     * Camera zooming
     *
     * @author boomboompower

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
    }  */

    /**
     * DrawBlockHighlight event
     *
     * @author Cubxity
     */
    @SuppressWarnings("Duplicates")
    @Overwrite
    public void renderWorldPass(int pass, float partialTicks, long finishTimeNano) {
        RenderGlobal renderglobal = this.mc.renderGlobal;
        EffectRenderer effectrenderer = this.mc.effectRenderer;
        boolean flag = this.isDrawBlockOutline();
        GlStateManager.enableCull();
        this.mc.mcProfiler.endStartSection("clear");
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        this.updateFogColor(partialTicks);
        GlStateManager.clear(16640);
        this.mc.mcProfiler.endStartSection("camera");
        this.setupCameraTransform(partialTicks, pass);
        ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
        this.mc.mcProfiler.endStartSection("frustum");
        ClippingHelperImpl.getInstance();
        this.mc.mcProfiler.endStartSection("culling");
        ICamera icamera = new Frustum();
        Entity entity = this.mc.getRenderViewEntity();
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
        icamera.setPosition(d0, d1, d2);

        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            this.setupFog(-1, partialTicks);
            this.mc.mcProfiler.endStartSection("sky");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
            GlStateManager.matrixMode(5888);
            renderglobal.renderSky(partialTicks, pass);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2);
            GlStateManager.matrixMode(5888);
        }

        this.setupFog(0, partialTicks);
        GlStateManager.shadeModel(7425);

        if (entity.posY + (double) entity.getEyeHeight() < 128.0D) {
            this.renderCloudsCheck(renderglobal, partialTicks, pass);
        }

        this.mc.mcProfiler.endStartSection("prepareterrain");
        this.setupFog(0, partialTicks);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        RenderHelper.disableStandardItemLighting();
        this.mc.mcProfiler.endStartSection("terrain_setup");
        renderglobal.setupTerrain(entity, (double) partialTicks, icamera, this.frameCount++, this.mc.thePlayer.isSpectator());

        if (pass == 0 || pass == 2) {
            this.mc.mcProfiler.endStartSection("updatechunks");
            this.mc.renderGlobal.updateChunks(finishTimeNano);
        }

        this.mc.mcProfiler.endStartSection("terrain");
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double) partialTicks, pass, entity);
        GlStateManager.enableAlpha();
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double) partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double) partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1F);

        if (!this.debugView) {
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("entities");
            renderglobal.renderEntities(entity, icamera, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.disableLightmap();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();

            if (this.mc.objectMouseOver != null && entity.isInsideOfMaterial(Material.water) && flag) {
                EntityPlayer entityplayer = (EntityPlayer) entity;
                GlStateManager.disableAlpha();
                this.mc.mcProfiler.endStartSection("outline");
                DrawBlockHighlightEvent event = new DrawBlockHighlightEvent(renderglobal, entityplayer, mc.objectMouseOver, 0, entityplayer.getHeldItem(), partialTicks);
                EventBus.INSTANCE.post(event);
                if (!event.isCancelled())
                    renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
                GlStateManager.enableAlpha();
            }
        }

        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();

        if (flag && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(Material.water)) {
            EntityPlayer entityplayer1 = (EntityPlayer) entity;
            GlStateManager.disableAlpha();
            this.mc.mcProfiler.endStartSection("outline");
            DrawBlockHighlightEvent event = new DrawBlockHighlightEvent(renderglobal, entityplayer1, mc.objectMouseOver, 0, entityplayer1.getHeldItem(), partialTicks);
            EventBus.INSTANCE.post(event);
            if (!event.isCancelled())
                renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, partialTicks);
            GlStateManager.enableAlpha();
        }

        this.mc.mcProfiler.endStartSection("destroyProgress");
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), entity, partialTicks);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        GlStateManager.disableBlend();

        if (!this.debugView) {
            this.enableLightmap();
            this.mc.mcProfiler.endStartSection("litParticles");
            effectrenderer.renderLitParticles(entity, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, partialTicks);
            this.mc.mcProfiler.endStartSection("particles");
            effectrenderer.renderParticles(entity, partialTicks);
            this.disableLightmap();
        }

        GlStateManager.depthMask(false);
        GlStateManager.enableCull();
        this.mc.mcProfiler.endStartSection("weather");
        this.renderRainSnow(partialTicks);
        GlStateManager.depthMask(true);
        renderglobal.renderWorldBorder(entity, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.alphaFunc(516, 0.1F);
        this.setupFog(0, partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        GlStateManager.shadeModel(7425);
        this.mc.mcProfiler.endStartSection("translucent");
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double) partialTicks, pass, entity);
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();

        if (entity.posY + (double) entity.getEyeHeight() >= 128.0D) {
            this.mc.mcProfiler.endStartSection("aboveClouds");
            this.renderCloudsCheck(renderglobal, partialTicks, pass);
        }

        this.mc.mcProfiler.endStartSection("hand");

        if (this.renderHand) {
            GlStateManager.clear(256);
            this.renderHand(partialTicks, pass);
            this.renderWorldDirections(partialTicks);
        }
    }
}