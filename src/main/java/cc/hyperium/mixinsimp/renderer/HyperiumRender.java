package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.mixins.renderer.IMixinRender;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.StaffUtils;
import cc.hyperium.utils.Utils;
import net.minecraft.client.Minecraft;
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

import java.awt.*;
import java.util.UUID;

public class HyperiumRender<T extends Entity> {

    private Render<T> parent;

    public HyperiumRender(Render<T> parent) {
        this.parent = parent;
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
            FontRenderer fontrenderer =renderManager.getFontRenderer();
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
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 0;

            if (str.equals("deadmau5")) {
                i = -10;
            }

            int j = fontrenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            float a = .25F;
            worldrenderer.pos((double) (-j - 1), (double) (-1), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
            worldrenderer.pos((double) (-j - 1), (double) (8), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (8), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (-1), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();

            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, -1);

            if (Settings.SHOW_ONLINE_PLAYERS && Settings.SHOW_DOTS_ON_NAME_TAGS && entityIn instanceof EntityPlayer) {
                String s = "⚫";
                UUID gameProfileId = ((EntityPlayer) entityIn).getGameProfile().getId();
                boolean online = Hyperium.INSTANCE.getHandlers().getStatusHandler().isOnline(gameProfileId);
                if (StaffUtils.isStaff(gameProfileId)) {
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

            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
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
}
