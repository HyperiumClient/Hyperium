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

package cc.hyperium.mixins.client.renderer.entity;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

import static cc.hyperium.config.Settings.*;

@Mixin(RenderFish.class)
public class MixinRenderFish extends Render<EntityFishHook> {

    @Shadow
    @Final
    private static ResourceLocation FISH_PARTICLES;

    protected MixinRenderFish(RenderManager renderManager) {
        super(renderManager);
    }

    /**
     * @author Mojang
     */
    @Overwrite
    public void doRender(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glLineWidth((float) Settings.ROD_COLOR_ROPE_WIDTH);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        bindEntityTexture(entity);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        worldrenderer.pos(-0.5D, -0.5D, 0.0D).tex(0.0625D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(0.5D, -0.5D, 0.0D).tex(0.125D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(0.5D, 0.5D, 0.0D).tex(0.125D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(-0.5D, 0.5D, 0.0D).tex(0.0625D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        if (entity.angler != null) {
            float f7 = entity.angler.getSwingProgress(partialTicks);
            float f8 = MathHelper.sin(MathHelper.sqrt_float(f7) * (float) Math.PI);
            Vec3 vec3 = new Vec3(-0.36D, 0.03D, 0.35D);
            vec3 = vec3.rotatePitch(-(entity.angler.prevRotationPitch + (entity.angler.rotationPitch - entity.angler.prevRotationPitch) * partialTicks) * (float) Math.PI / 180.0F);
            vec3 = vec3.rotateYaw(-(entity.angler.prevRotationYaw + (entity.angler.rotationYaw - entity.angler.prevRotationYaw) * partialTicks) * (float) Math.PI / 180.0F);
            vec3 = vec3.rotateYaw(f8 * 0.5F);
            vec3 = vec3.rotatePitch(-f8 * 0.7F);
            double d0 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * (double) partialTicks + vec3.xCoord;
            double d1 = entity.angler.prevPosY + (entity.angler.posY - entity.angler.prevPosY) * (double) partialTicks + vec3.yCoord;
            double d2 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * (double) partialTicks + vec3.zCoord;
            double d3 = entity.angler.getEyeHeight();

            if (renderManager.options != null && renderManager.options.thirdPersonView > 0 || entity.angler != Minecraft.getMinecraft().thePlayer) {
                float f9 = (entity.angler.prevRenderYawOffset + (entity.angler.renderYawOffset - entity.angler.prevRenderYawOffset) * partialTicks) * (float) Math.PI / 180.0F;
                double d4 = MathHelper.sin(f9);
                double d6 = MathHelper.cos(f9);
                d0 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * (double) partialTicks - d6 * 0.35D - d4 * 0.8D;
                d1 = entity.angler.prevPosY + d3 + (entity.angler.posY - entity.angler.prevPosY) * (double) partialTicks - 0.45D;
                d2 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * (double) partialTicks - d4 * 0.35D + d6 * 0.8D;
                d3 = entity.angler.isSneaking() ? -0.1875D : 0.0D;
            }

            double d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
            double d5 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + 0.25D;
            double d7 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
            double d9 = (float) (d0 - d13);
            double d11 = (double) ((float) (d1 - d5)) + d3;
            double d12 = (float) (d2 - d7);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);

            for (int l = 0; l <= 16; ++l) {
                float f10 = (float) l / 16.0F;
                if (Settings.ROD_COLOR_CHROMA) {
                    long dif = (long) ((x * 10) - (y * 10));
                    long color = System.currentTimeMillis() - dif;
                    int i = Color.HSBtoRGB((float) (color % (int) 2000F) / 2000F, 0.8F, 0.8F);
                    Color c = new Color(i);
                    worldrenderer.pos(x + d9 * (double) f10, y + d11 * (double) (f10 * f10 + f10) * 0.5D + 0.25D, z + d12 * (double) f10).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
                } else
                    worldrenderer.pos(x + d9 * (double) f10, y + d11 * (double) (f10 * f10 + f10) * 0.5D + 0.25D, z + d12 * (double) f10).color((int) ROD_COLOR_RED, (int) ROD_COLOR_GREEN, (int) ROD_COLOR_BLUE, (int) ROD_COLOR_ALPHA).endVertex();
            }

            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    /**
     * @author Mojang
     */
    @Overwrite
    protected ResourceLocation getEntityTexture(EntityFishHook entity) {
        return FISH_PARTICLES;
    }
}
