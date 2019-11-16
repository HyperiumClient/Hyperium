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
import cc.hyperium.event.EventBus;
import cc.hyperium.event.render.RenderNameTagEvent;
import cc.hyperium.mixins.client.renderer.entity.IMixinRender;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.StaffUtils;
import cc.hyperium.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.UUID;

public class HyperiumRender<T extends Entity> {

    private Render<T> parent;

    public HyperiumRender(Render<T> parent) {
        this.parent = parent;
    }

    private static void drawChromaWaveString(String text, int xIn, int y) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        int x = xIn;
        for (char c : text.toCharArray()) {
            long dif = (x * 10) - (y * 10);
            long l = System.currentTimeMillis() - dif;
            float ff = 2000.0F;
            int i = Color.HSBtoRGB((float) (l % (int) ff) / ff, 0.8F, 0.8F);
            String tmp = String.valueOf(c);
            renderer.drawString(tmp, (float) ((double) x), (float) ((double) y), i, false);
            x += (double) renderer.getCharWidth(c);
        }
    }

    public void renderOffsetLivingLabel(T entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
        ((IMixinRender) parent).callRenderLivingLabel(entityIn, str, x, y, z, Math.min(64 * 64, Hyperium.INSTANCE.getHandlers().getConfigOptions().renderNameDistance));
    }

    public void renderName(T entity, double x, double y, double z) {
        if (((IMixinRender) parent).callCanRenderName(entity)) {
            ((IMixinRender) parent).callRenderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, Math.min(64 * 64, Hyperium.INSTANCE.getHandlers().getConfigOptions().renderNameDistance));
        }
    }

    public void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance, RenderManager renderManager) {
        double d0 = entityIn.getDistanceSqToEntity(renderManager.livingPlayer);

        if (d0 <= (double) (maxDistance * maxDistance)) {
            boolean self = entityIn.equals(Minecraft.getMinecraft().thePlayer);
            boolean show = !self || Settings.SHOW_OWN_NAME || RenderNameTagEvent.CANCEL;
            FontRenderer fontrenderer = renderManager.getFontRenderer();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GlStateManager.pushMatrix();
            float offset = Utils.INSTANCE.calculateDeadmauEarsOffset(entityIn);
            GlStateManager.translate((float) x + 0.0F, (float) y + offset + entityIn.height + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

            int xMultiplier = 1; // Nametag x rotations should flip in front-facing 3rd person
            if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().gameSettings != null && Minecraft.getMinecraft().gameSettings.thirdPersonView == 2)
                xMultiplier = -1;
            GlStateManager.rotate(renderManager.playerViewX * xMultiplier, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

            if (show) {
                int j = fontrenderer.getStringWidth(str) / 2;
                GlStateManager.disableTexture2D();
                worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                float a = .25F;
                worldrenderer.pos(-j - 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
                worldrenderer.pos(-j - 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
                worldrenderer.pos(j + 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
                worldrenderer.pos(j + 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
                tessellator.draw();
            }
            GlStateManager.enableTexture2D();
            if (show)
                fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            if (show)
                fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, -1);
            if (show)
                if (Settings.SHOW_ONLINE_PLAYERS && Settings.SHOW_DOTS_ON_NAME_TAGS && entityIn instanceof EntityPlayer) {
                    String s = "⚫";
                    UUID gameProfileId = ((EntityPlayer) entityIn).getGameProfile().getId();
                    boolean online = Hyperium.INSTANCE.getHandlers().getStatusHandler().isOnline(gameProfileId);
                    if (StaffUtils.isStaff(gameProfileId) || StaffUtils.isBooster(gameProfileId)) {
                        StaffUtils.DotColour colour = StaffUtils.getColor(gameProfileId);
                        if (colour.isChroma) {
                            drawChromaWaveString(s, (fontrenderer.getStringWidth(str) + fontrenderer.getStringWidth(s)) / 2, -2);
                        } else {
                            String format = StaffUtils.getColor(gameProfileId).baseColour + s;
                            fontrenderer.drawString(format, (fontrenderer.getStringWidth(str) + fontrenderer.getStringWidth(s)) / 2, -2, Color.WHITE.getRGB());
                        }
                    } else {
                        String format = online ? ChatColor.GREEN + s : ChatColor.RED + s;
                        fontrenderer.drawString(format, (fontrenderer.getStringWidth(str) + fontrenderer.getStringWidth(s)) / 2, -2, Color.WHITE.getRGB());
                    }
                }
            if (entityIn instanceof EntityPlayer && !RenderNameTagEvent.CANCEL) {
                EventBus.INSTANCE.post(new RenderNameTagEvent(((AbstractClientPlayer) entityIn), renderManager));
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.popMatrix();
        }
    }
}
