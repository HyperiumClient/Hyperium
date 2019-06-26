package cc.hyperium.mods.levelhead.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.display.AboveHeadDisplay;
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

            if (index > extraHead) {
                continue;
            }

            if (!display.getConfig().isEnabled()) {
                continue;
            }

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

                    if (event.getEntity().getUniqueID().equals(Levelhead.getInstance().userUuid)) {
                        offset = 0;
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
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private void renderString(FontRenderer fontrenderer, LevelheadTag tag) {
        int y = 0;

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
            fontRenderer.drawString(header.getValue(), x, y, new Color((float) header.getRed() / 255F, (float) header.getGreen() / 255F, (float) header.getBlue() / 255F, .2F).getRGB());
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
