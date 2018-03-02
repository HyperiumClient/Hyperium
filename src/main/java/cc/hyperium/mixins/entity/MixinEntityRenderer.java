/*
 * Hyperium Client, Free client with huds and popular mod
 * Copyright (C) 2018  Hyperium Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package cc.hyperium.mixins.entity;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V", shift = At.Shift.AFTER))
    private void updateCameraAndRender(float partialTicks, long nano, CallbackInfo ci) {
        EventBus.INSTANCE.post(new RenderEvent());
    }

    @Shadow
    public static boolean anaglyphEnable;
    @Shadow
    private float thirdPersonDistance;
    @Shadow
    private float thirdPersonDistanceTemp;
    @Shadow
    private boolean cloudFog;
    @Shadow
    private long prevFrameTime;
    @Shadow
    private float smoothCamYaw;
    @Shadow
    private float smoothCamPitch;
    @Shadow
    private float smoothCamFilterX;
    @Shadow
    private float smoothCamFilterY;
    @Shadow
    private float smoothCamPartialTicks;
    @Shadow
    private ShaderGroup theShaderGroup;
    @Shadow
    private boolean useShader;
    @Shadow
    private long renderEndNanoTime;

    @Shadow
    public abstract void setupOverlayRendering();

    @Shadow
    public abstract void renderWorld(float partialTicks, long finishTimeNano);
    
    @Shadow private boolean debugView;
    
    @Shadow private Minecraft mc;
    
    @Shadow private float fovModifierHandPrev;
    
    @Shadow private float fovModifierHand;
    
    @Shadow private MouseFilter mouseFilterYAxis;
    
    @Shadow private MouseFilter mouseFilterXAxis;
    
    private boolean zoomMode = false;
    
    /**
     * @author COAL UR FUCKING CODE IS SHIT
     */
    @Overwrite
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
            double d3 = (double) (this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks);

            if (Minecraft.getMinecraft().gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
            } else {
                float f1 = entity.rotationYaw;
                float f2 = entity.rotationPitch;

                if (Hyperium.perspective.enabled) {
                    f1 = Hyperium.perspective.modifiedYaw;
                    f2 = Hyperium.perspective.modifiedPitch;
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

                if (Hyperium.perspective.enabled) {
                    GlStateManager.rotate(Hyperium.perspective.modifiedPitch - f2, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(Hyperium.perspective.modifiedYaw - f1, 0.0F, 1.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
                    GlStateManager.rotate(f1 - Hyperium.perspective.modifiedYaw, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f2 - Hyperium.perspective.modifiedPitch, 1.0F, 0.0F, 0.0F);
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
            if (Hyperium.perspective.enabled) {
                GlStateManager.rotate(Hyperium.perspective.modifiedPitch, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(Hyperium.perspective.modifiedYaw + 180.0F, 0.0F, 1.0F, 0.0F);
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
    }

    /**
     * @author COAL UR FUCKING CODE IS SHIT
     */
    @Overwrite
    public void updateCameraAndRender(float p_181560_1_, long p_181560_2_) {
        boolean flag = Display.isActive();

        if (!flag && Minecraft.getMinecraft().gameSettings.pauseOnLostFocus && (!Minecraft.getMinecraft().gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
                Minecraft.getMinecraft().displayInGameMenu();
            }
        } else {
            this.prevFrameTime = Minecraft.getSystemTime();
        }

        Minecraft.getMinecraft().mcProfiler.startSection("mouse");

        if (flag && Minecraft.isRunningOnMac && Minecraft.getMinecraft().inGameHasFocus && !Mouse.isInsideWindow()) {
            Mouse.setGrabbed(false);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
            Mouse.setGrabbed(true);
        }

        if (Minecraft.getMinecraft().inGameHasFocus && flag) {
            if (Hyperium.perspective.enabled && Minecraft.getMinecraft().gameSettings.thirdPersonView != 1) {
                Hyperium.perspective.onDisable();
            }

            Minecraft.getMinecraft().mouseHelper.mouseXYChange();
            float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f1 = f * f * f * 8.0F;
            float f2 = (float) Minecraft.getMinecraft().mouseHelper.deltaX * f1;
            float f3 = (float) Minecraft.getMinecraft().mouseHelper.deltaY * f1;
            int i = 1;

            if (Minecraft.getMinecraft().gameSettings.invertMouse) {
                i = -1;
            }

            if (Minecraft.getMinecraft().gameSettings.smoothCamera) {
                this.smoothCamYaw += f2;
                this.smoothCamPitch += f3;
                float f4 = p_181560_1_ - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = p_181560_1_;
                f2 = this.smoothCamFilterX * f4;
                f3 = this.smoothCamFilterY * f4;

                if (Hyperium.perspective.enabled) {

                    // Modifying pitch and yaw values.
                    Hyperium.perspective.modifiedYaw += f2 / 8.0F;
                    Hyperium.perspective.modifiedPitch += f3 / 8.0F;


                    // Range check.
                    if (Math.abs(Hyperium.perspective.modifiedPitch) > 90.0F) {
                        if (Hyperium.perspective.modifiedPitch > 0.0F) {
                            Hyperium.perspective.modifiedPitch = 90.0F;
                        } else {
                            Hyperium.perspective.modifiedPitch = -90.0F;
                        }
                    }
                } else {
                    Minecraft.getMinecraft().thePlayer.setAngles(f2, f3 * (float) i);
                }
            } else if (Hyperium.perspective.enabled) {
                // Modifying pitch and yaw values.
                Hyperium.perspective.modifiedYaw += f2 / 8.0F;
                Hyperium.perspective.modifiedPitch += f3 / 8.0F;

                // Checking if pitch exceeds maximum range.
                if (Math.abs(Hyperium.perspective.modifiedPitch) > 90.0F) {
                    if (Hyperium.perspective.modifiedPitch > 0.0F) {
                        Hyperium.perspective.modifiedPitch = 90.0F;
                    } else {
                        Hyperium.perspective.modifiedPitch = -90.0F;
                    }
                }
            } else {
                this.smoothCamYaw = 0.0F;
                this.smoothCamPitch = 0.0F;
                Minecraft.getMinecraft().thePlayer.setAngles(f2, f3 * (float) i);
            }
        }

        Minecraft.getMinecraft().mcProfiler.endSection();

        if (!Minecraft.getMinecraft().skipRenderWorld) {
            anaglyphEnable = Minecraft.getMinecraft().gameSettings.anaglyph;
            final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            int i1 = scaledresolution.getScaledWidth();
            int j1 = scaledresolution.getScaledHeight();
            final int k1 = Mouse.getX() * i1 / Minecraft.getMinecraft().displayWidth;
            final int l1 = j1 - Mouse.getY() * j1 / Minecraft.getMinecraft().displayHeight - 1;
            int i2 = Minecraft.getMinecraft().gameSettings.limitFramerate;

            if (Minecraft.getMinecraft().theWorld != null) {
                Minecraft.getMinecraft().mcProfiler.startSection("level");
                int j = Math.min(Minecraft.getDebugFPS(), i2);
                j = Math.max(j, 60);
                long k = System.nanoTime() - p_181560_2_;
                long l = Math.max((long) (1000000000 / j / 4) - k, 0L);
                this.renderWorld(p_181560_1_, System.nanoTime() + l);

                if (OpenGlHelper.shadersSupported) {
                    Minecraft.getMinecraft().renderGlobal.renderEntityOutlineFramebuffer();

                    if (this.theShaderGroup != null && this.useShader) {
                        GlStateManager.matrixMode(5890);
                        GlStateManager.pushMatrix();
                        GlStateManager.loadIdentity();
                        this.theShaderGroup.loadShaderGroup(p_181560_1_);
                        GlStateManager.popMatrix();
                    }

                    Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
                }

                this.renderEndNanoTime = System.nanoTime();
                Minecraft.getMinecraft().mcProfiler.endStartSection("gui");

                if (!Minecraft.getMinecraft().gameSettings.hideGUI || Minecraft.getMinecraft().currentScreen != null) {
                    GlStateManager.alphaFunc(516, 0.1F);
                    Minecraft.getMinecraft().ingameGUI.renderGameOverlay(p_181560_1_);
                }

                Minecraft.getMinecraft().mcProfiler.endSection();
            } else {
                GlStateManager.viewport(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                this.setupOverlayRendering();
                this.renderEndNanoTime = System.nanoTime();
            }

            if (Minecraft.getMinecraft().currentScreen != null) {
                GlStateManager.clear(256);

                try {
                    Minecraft.getMinecraft().currentScreen.drawScreen(k1, l1, p_181560_1_);
                } catch (Throwable throwable) {
                    // This is necessary because it crashes otherwise for some reason... Unsure if this will cause any issues in the future.
                }
            }
        }
    }
    
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