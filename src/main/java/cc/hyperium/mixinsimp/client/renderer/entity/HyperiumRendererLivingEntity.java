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

package cc.hyperium.mixinsimp.client.renderer.entity;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.mixins.client.renderer.entity.IMixinRender;
import cc.hyperium.mixins.client.renderer.entity.IMixinRendererLivingEntity;
import cc.hyperium.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class HyperiumRendererLivingEntity<T extends EntityLivingBase> {

    private RendererLivingEntity<T> parent;

    public HyperiumRendererLivingEntity(RendererLivingEntity<T> rendererLivingEntity) {
        parent = rendererLivingEntity;
    }

    public void renderLayers(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                             float netHeadYaw, float headPitch, float scale, List<LayerRenderer<T>> layerRenderers) {
        for (LayerRenderer<T> layerrenderer : layerRenderers) {
            boolean f = layerrenderer.shouldCombineTextures();
            if (Settings.OLD_ARMOUR) f = true;
            boolean flag = ((IMixinRendererLivingEntity<T>) parent).callSetBrightness(entitylivingbaseIn, partialTicks, f);
            layerrenderer.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            if (flag) ((IMixinRendererLivingEntity) parent).callUnsetBrightness();
        }
    }

    public void rotateCorpse(T bat, float rotation, float partialTicks) {
        GlStateManager.rotate(180.0F - rotation, 0.0F, 1.0F, 0.0F);

        if (bat.deathTime > 0) {
            float f = ((float) bat.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt_float(f);
            if (f > 1.0F) f = 1.0F;

            GlStateManager.rotate(f * ((IMixinRendererLivingEntity<T>) parent).callGetDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
        } else {
            Hyperium.INSTANCE.getHandlers().getFlipHandler().transform(bat);
        }
    }

    public void renderName(T entity, double x, double y, double z, RenderManager renderManager) {
        if (((IMixinRendererLivingEntity<T>) parent).callCanRenderName(entity)) {

            double d0 = entity.getDistanceSqToEntity(renderManager.livingPlayer);
            float f = entity.isSneaking() ? 32.0F : 64.0F;

            if (d0 < (double) (f * f)) {
                String s = entity.getDisplayName().getFormattedText();
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

                if (entity.isSneaking() && (Settings.SHOW_OWN_NAME || !entity.equals(Minecraft.getMinecraft().thePlayer))) {
                    FontRenderer fontrenderer = renderManager.getFontRenderer();
                    GlStateManager.pushMatrix();
                    float offset = Utils.INSTANCE.calculateDeadmauEarsOffset(entity);
                    GlStateManager.translate((float) x, (float) y + offset + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float) z);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
                    GlStateManager.translate(0.0F, 9.374999F, 0.0F);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.disableTexture2D();
                    GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                    int i = fontrenderer.getStringWidth(s) / 2;
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                    GlStateManager.depthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.popMatrix();
                } else {
                    ((IMixinRender<T>) parent).callRenderOffsetLivingLabel(entity, x, y - (entity.isChild() ? (double)
                        (entity.height / 2.0F) : 0.0D), z, s, 0.02666667F, d0);
                }
            }
        }
    }
}
