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

package cc.hyperium.mods.levelhead.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderNameTagEvent;
import cc.hyperium.mods.levelhead.Levelhead;
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

import java.awt.Color;

/**
 * Created by mitchellkatz
 * <p>
 * Modified by boomboompower on 16/6/2017
 */
public class LevelHeadRender {

    private final Levelhead levelHead;

    public LevelHeadRender(Levelhead levelHead) {
        this.levelHead = levelHead;
    }

    @InvokeEvent
    public void render(RenderNameTagEvent event) {

        if ((event.getEntity().getUniqueID().equals(this.levelHead.userUuid) && !levelHead.getConfig().isShowSelf()) || !Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel())
            return;

        EntityPlayer player = event.getEntity();

        if (levelHead.loadOrRender(player) && (this.levelHead.getLevelString(player.getUniqueID())) != null) {
            if (player.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) < 64 * 64) {
                double offset = 0.3;
                Scoreboard scoreboard = player.getWorldScoreboard();
                ScoreObjective scoreObjective = scoreboard.getObjectiveInDisplaySlot(2);

                if (scoreObjective != null && player.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) < 10 * 10) {
                    offset *= 2;
                }
                if (player.getUniqueID().equals(this.levelHead.userUuid) && !Settings.SHOW_OWN_NAME)
                    offset = -10;
                if (Hyperium.INSTANCE.getCosmetics().getDeadmau5Cosmetic().isPurchasedBy(event.getEntity().getUniqueID())) {
                    HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(event.getEntity().getUniqueID());
                    if (packageIfReady != null) {
                        AbstractHyperiumPurchase purchase = packageIfReady.getPurchase(EnumPurchaseType.DEADMAU5_COSMETIC);
                        if (purchase != null) {
                            if (event.getEntity().getUniqueID() != Minecraft.getMinecraft().thePlayer.getUniqueID()) {
                                if (((EarsCosmetic) purchase).isEnabled()) {
                                    offset += .3;
                                }
                            } else if (Settings.EARS_STATE.equalsIgnoreCase("on"))
                                offset += .2;
                        }

                    }
                }
                renderName(event, (this.levelHead.getLevelString(player.getUniqueID())), player, offset);
            }
        }
    }

    public void renderName(RenderNameTagEvent event, LevelheadTag tag, EntityPlayer entityIn, double offset) {
        FontRenderer fontrenderer = event.getRenderManager().getFontRenderer();
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.translate(0, -10 - offset, 0);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);


//        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;

        int j = fontrenderer.getStringWidth(tag.getString()) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double) (-j - 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (-j - 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (j + 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (j + 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        renderString(fontrenderer, tag);

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
    }

    private void renderString(FontRenderer renderer, LevelheadTag tag) {

        int y = 0;

        int x = -renderer.getStringWidth(tag.getString()) / 2;
        //Render header
        LevelheadComponent header = tag.getHeader();
        render(renderer, header, x);
        x += renderer.getStringWidth(header.getValue());
        //render footer
        render(renderer, tag.getFooter(), x);

    }

    private void render(FontRenderer renderer, LevelheadComponent header, int x) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        int y = 0;
        if (header.isRgb()) {
//            GlStateManager.color(header.getRed()/2, header.getBlue()/2, header.getGreen()/2);
            renderer.drawString(header.getValue(), x, y, new Color((float) header.getRed() / 255F, (float) header.getGreen() / 255F, (float) header.getBlue() / 255F, .2F).getRGB());
        } else if (header.isChroma()) {
            renderer.drawString(header.getValue(), x, y, levelHead.getRGBDarkColor());
        } else {
            GlStateManager.color(255, 255, 255, .5F);
            renderer.drawString(header.getColor() + header.getValue(), x, y, Color.WHITE.darker().darker().darker().darker().darker().getRGB() * 255);

        }
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);

        GlStateManager.color(1.0F, 1.0F, 1.0F);
        if (header.isRgb()) {
            GlStateManager.color(header.getRed(), header.getBlue(), header.getGreen(), header.getAlpha());
            renderer.drawString(header.getValue(), x, y, new Color(header.getRed(), header.getGreen(), header.getBlue()).getRGB());
        } else if (header.isChroma()) {
            renderer.drawString(header.getValue(), x, y, header.isChroma() ? levelHead.getRGBColor() : 553648127);
        } else {
            GlStateManager.color(255, 255, 255, .5F);
            String text = header.getColor() + header.getValue();
            renderer.drawString(text, x, y, Color.WHITE.darker().getRGB());
        }


    }
}
