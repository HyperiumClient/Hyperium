package cc.hyperium.mods.fortnitecompass;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.mods.AbstractMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class FortniteCompassMod extends AbstractMod {

    private static int offY = 0;
    private static int cwidth = 500;
    private static int offsetAll = 0;
    private static int centerX = 0;
    private static int colorMarker = 0;
    private static int colorDirection = 0;
    public static int width = 184;
    public static int height = 20;

    private Minecraft mc = Minecraft.getMinecraft();
    private FontRenderer fr;

    @Override
    public AbstractMod init() {
        this.fr = mc.fontRendererObj;
        EventBus.INSTANCE.register(this);
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "FortniteCompassMod", "1.0", "CanalexMC, ConorTheDev & KiritoDev");
    }

    private void drawCompass(int screenWidth) {
        int direction = normalize((int) this.mc.thePlayer.rotationYaw);
        offsetAll = (cwidth * direction / 360);
        int offX = 0;
        centerX = (screenWidth / 2 + offX);
        if (Settings.FNCOMPASS_BACKGROUND) {
            Gui.drawRect(centerX - width / 2, offY, centerX + width / 2, offY + height, -1442840576);
        }
        if (!Settings.FNCOMPASS_CHROMA) {
            int tintMarker = 0;
            if (tintMarker != 0) {
                colorMarker = Color.HSBtoRGB(tintMarker / 100.0F, 1.0F, 1.0F);
            } else {
                colorMarker = -1;
            }
            int tintDirection = 0;
            if (tintDirection != 0) {
                colorDirection = Color.HSBtoRGB(tintDirection / 100.0F, 1.0F, 1.0F);
            } else {
                colorDirection = -1;
            }
        } else {
            colorDirection = (colorMarker = Color.HSBtoRGB((float) (System.currentTimeMillis() % 3000L) / 3000.0F, 1.0F, 1.0F));
        }
        renderMarker();
        if (Integer.parseInt(Settings.FNCOMPASS_DETAILS) >= 0) {
            drawDirection("S", 0, 1.5D);
            drawDirection("W", 90, 1.5D);
            drawDirection("N", 180, 1.5D);
            drawDirection("E", 270, 1.5D);
        }
        if (Integer.parseInt(Settings.FNCOMPASS_DETAILS) >= 1) {
            drawDirection("SW", 45, 1.0D);
            drawDirection("NW", 135, 1.0D);
            drawDirection("NE", 225, 1.0D);
            drawDirection("SE", 315, 1.0D);
        }
        if (Integer.parseInt(Settings.FNCOMPASS_DETAILS) >= 2) {
            drawDirection("15", 15, 0.75D);
            drawDirection("30", 30, 0.75D);
            drawDirection("60", 60, 0.75D);
            drawDirection("75", 75, 0.75D);
            drawDirection("105", 105, 0.75D);
            drawDirection("120", 120, 0.75D);
            drawDirection("150", 150, 0.75D);
            drawDirection("165", 165, 0.75D);
            drawDirection("195", 195, 0.75D);
            drawDirection("210", 210, 0.75D);
            drawDirection("240", 240, 0.75D);
            drawDirection("255", 255, 0.75D);
            drawDirection("285", 285, 0.75D);
            drawDirection("300", 300, 0.75D);
            drawDirection("330", 330, 0.75D);
            drawDirection("345", 345, 0.75D);
        }
    }

    private void renderMarker() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        GlStateManager.color((colorMarker >> 16 & 0xFF) / 255.0F, (colorMarker >> 8 & 0xFF) / 255.0F, (colorMarker & 0xFF) / 255.0F, 1.0F);

        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        worldrenderer.pos(centerX, offY + 3, 0.0D).endVertex();
        worldrenderer.pos(centerX + 3, offY, 0.0D).endVertex();
        worldrenderer.pos(centerX - 3, offY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private void drawDirection(String dir, int degrees, double scale) {
        int offset = cwidth * degrees / 360 - offsetAll;
        if (offset > cwidth / 2) {
            offset -= cwidth;
        }
        if (offset < -cwidth / 2) {
            offset += cwidth;
        }
        double opacity = 1.0D - Math.abs(offset) / (width / 2.0D);
        if (opacity > 0.1D) {
            int defcolor = colorDirection & 0xFFFFFF;
            int color = defcolor | (int) (opacity * 255.0D) << 24;
            int posX = centerX + offset - (int) (this.fr.getStringWidth(dir) * scale / 2.0D);
            int posY = offY + height / 2 - (int) (this.fr.FONT_HEIGHT * scale / 2.0D);

            GL11.glEnable(3042);
            GL11.glPushMatrix();
            GL11.glTranslated(-posX * (scale - 1.0D), -posY * (scale - 1.0D), 0.0D);
            GL11.glScaled(scale, scale, 1.0D);
            if (Settings.FNCOMPASS_SHADOW) {
                this.fr.drawStringWithShadow(dir, posX, posY, color);
            } else {
                this.fr.drawString(dir, posX, posY, color);
            }
            GL11.glPopMatrix();
            GL11.glDisable(3042);
        }
    }

    private int normalize(int direction) {
        if (direction > 360) {
            direction %= 360;
        }
        while (direction < 0) {
            direction += 360;
        }
        return direction;
    }

    @InvokeEvent
    public void onRenderOverlay(final RenderHUDEvent event) {
        ScaledResolution sr = new ScaledResolution(mc);

        if (!Settings.FNCOMPASS_ENABLED) {
            return;
        }
        if (this.mc.thePlayer == null) {
            return;
        }
        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
            return;
        }
        drawCompass(sr.getScaledWidth());
    }
}
