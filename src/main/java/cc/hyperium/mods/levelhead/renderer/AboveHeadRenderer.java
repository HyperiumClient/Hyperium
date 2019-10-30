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

package cc.hyperium.mods.levelhead.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderPlayerEvent;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.display.AboveHeadDisplay;
import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.purchases.packages.EarsCosmetic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class AboveHeadRenderer {

    private Levelhead levelhead;

    public AboveHeadRenderer(Levelhead levelhead) {
        this.levelhead = levelhead;
    }

    @InvokeEvent
    public void render(RenderPlayerEvent event) {
        if (levelhead == null ||
            levelhead.getDisplayManager() == null ||
            levelhead.getDisplayManager().getMasterConfig() == null ||
            !levelhead.getDisplayManager().getMasterConfig().isEnabled()) {
            return;
        }

        EntityPlayer player = event.getEntity();
        int o = 0;

        for (AboveHeadDisplay display : levelhead.getDisplayManager().getAboveHead()) {
            int index = display.getIndex();
            int extraHead = levelhead.getLevelheadPurchaseStates().getExtraHead();
            if (index > extraHead || !display.getConfig().isEnabled()) continue;

            LevelheadTag levelheadTag = display.getCache().get(player.getUniqueID());

            if (display.loadOrRender(player) && levelheadTag != null && !(levelheadTag instanceof NullLevelheadTag)) {
                if ((event.getEntity().getUniqueID().equals(Levelhead.getInstance().userUuid) && !display.getConfig().isShowSelf()
                    || !Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel())) {
                    continue;
                }

                if (player.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) < 64 * 64) {
                    double offset = 0.3;
                    Scoreboard scoreboard = player.getWorldScoreboard();
                    ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(2);

                    if (objective != null && event.getEntity().getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) < 10 * 10) {
                        offset *= 2;
                    }

                    if (player.getUniqueID().equals(levelhead.userUuid) && !Settings.SHOW_OWN_NAME) offset -= .3;
                    if (Hyperium.INSTANCE.getCosmetics().getDeadmau5Cosmetic().isPurchasedBy(event.getEntity().getUniqueID())) {
                        HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(event.getEntity().getUniqueID());
                        if (packageIfReady != null) {
                            AbstractHyperiumPurchase purchase = packageIfReady.getPurchase(EnumPurchaseType.DEADMAU5_COSMETIC);
                            if (purchase != null) {
                                if (event.getEntity().getUniqueID() != Minecraft.getMinecraft().thePlayer.getUniqueID()) {
                                    if (((EarsCosmetic) purchase).isEnabled()) {
                                        offset += .3;
                                    }
                                } else if (Settings.EARS_STATE.equalsIgnoreCase("on")) {
                                    offset += .2;
                                }
                            }
                        }
                    }

                    offset += levelhead.getDisplayManager().getMasterConfig().getOffset();
                    renderName(event, levelheadTag, player, event.getX(), event.getY() + offset + o * .3D, event.getZ());
                }
            }

            o++;
        }
    }

    private void renderName(RenderPlayerEvent event, LevelheadTag tag, EntityPlayer player, double x, double y, double z) {
        FontRenderer fontrenderer = event.getRenderManager().getFontRenderer();
        float f = (float) (1.6F * Levelhead.getInstance().getDisplayManager().getMasterConfig().getFontSize());
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.0F, (float) y + player.height + 0.5F, (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-event.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(event.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;

        int j = fontrenderer.getStringWidth(tag.getString()) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        renderString(fontrenderer, tag);

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private void renderString(FontRenderer fontrenderer, LevelheadTag tag) {
        int x = -fontrenderer.getStringWidth(tag.getString()) / 2;
        LevelheadComponent header = tag.getHeader();
        render(fontrenderer, header, x);
        x += fontrenderer.getStringWidth(header.getValue());
        render(fontrenderer, tag.getFooter(), x);
    }

    private void render(FontRenderer fontRenderer, LevelheadComponent header, int x) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        int y = 0;
        if (header.isRgb()) {
            fontRenderer.drawString(header.getValue(), x, y, new Color((float) header.getRed() / 255F, (float) header.getGreen() / 255F,
                (float) header.getBlue() / 255F, .2F).getRGB());
        } else if (header.isChroma()) {
            fontRenderer.drawString(header.getValue(), x, y, Levelhead.getInstance().getDarkRGBColor());
        } else {
            GlStateManager.color(255, 255, 255, .5F);
            fontRenderer.drawString(header.getColor() + header.getValue(), x, y, Color.WHITE.darker().darker().darker().darker().darker().getRGB() * 255);
        }
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);

        GlStateManager.color(1.0F, 1.0F, 1.0F);
        if (header.isRgb()) {
            GlStateManager.color(header.getRed(), header.getBlue(), header.getGreen(), header.getAlpha());
            fontRenderer.drawString(header.getValue(), x, y, new Color(header.getRed(), header.getGreen(), header.getBlue()).getRGB());
        } else if (header.isChroma()) {
            fontRenderer.drawString(header.getValue(), x, y, header.isChroma() ? Levelhead.getInstance().getRGBColor() : 553648127);
        } else {
            GlStateManager.color(255, 255, 255, .5F);

            fontRenderer.drawString(header.getColor() + header.getValue(), x, y, Color.WHITE.darker().getRGB());
        }
    }
}
