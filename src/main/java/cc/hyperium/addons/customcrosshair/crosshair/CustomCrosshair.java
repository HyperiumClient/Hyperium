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

package cc.hyperium.addons.customcrosshair.crosshair;

import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderHUDEvent;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CustomCrosshair {

    public static boolean showVanillaCrosshair = true;
    private boolean enabled;
    private Color colour;
    private CrosshairType crosshairType;
    private boolean visibleHiddenGui;
    private boolean visibleDefault;
    private boolean visibleDebug;
    private boolean visibleSpectator;
    private boolean visibleThirdPerson;
    private boolean outline;
    private Color colourOutline;
    private boolean dot;
    private Color colourDot;
    private int width;
    private int height;
    private int gap;
    private int thickness;
    private boolean dynamicBow;
    private boolean rainbowCrosshair;
    private int rainbowSpeed;
    private int rainbowColourTick;
    private Minecraft mc;

    public CustomCrosshair() {
        mc = Minecraft.getMinecraft();
        crosshairType = CrosshairType.CROSS;
        setEnabled(false);
        colour = new Color(255, 255, 255, 255);
        visibleDefault = true;
        visibleHiddenGui = true;
        visibleDebug = true;
        visibleSpectator = true;
        visibleThirdPerson = false;
        outline = true;
        colourOutline = new Color(0, 0, 0, 255);
        dot = false;
        colourDot = new Color(255, 255, 255, 255);
        width = 5;
        height = 5;
        gap = 3;
        thickness = 1;
        dynamicBow = true;
        setRainbowCrosshair(false);
        rainbowSpeed = 500;
        rainbowColourTick = 0;
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent event) {
        if (mc.theWorld != null && mc.currentScreen == null && enabled) {
            CustomCrosshairAddon.getCrosshairMod().getCrosshair().drawCrosshair();
        }
    }

    public void drawCrosshair() {
        int screenWidth = ResolutionUtil.current().getScaledWidth() / 2;
        int screenHeight = ResolutionUtil.current().getScaledHeight() / 2;
        if (!enabled && !mc.gameSettings.hideGUI) {
            if (mc.gameSettings.showDebugInfo) {
                drawDebugAxisCrosshair(screenWidth, screenHeight);
            }
        }
        if (enabled && (mc.gameSettings.thirdPersonView <= 0 || visibleThirdPerson) && (!mc.gameSettings.hideGUI || visibleHiddenGui) && (
            !mc.thePlayer
                .isSpectator() || visibleSpectator)) {
            if (!visibleDefault) {
                return;
            }
            if (mc.gameSettings.hideGUI) {
                GlStateManager.clear(GL11.GL_ACCUM);
                GlStateManager.matrixMode(GL11.GL_PROJECTION);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0, ResolutionUtil.current().getScaledWidth_double(),
                    ResolutionUtil.current().getScaledHeight_double(), 0.0, 1000.0, 3000.0);
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            }
            ++rainbowColourTick;
            Color renderColour = colour;
            int renderGap = gap;
            if (rainbowCrosshair) {
                int red = (int) (
                    Math.sin(rainbowSpeed / 100000.0f * rainbowColourTick + 0.0f) * 127.0
                        + 128.0);
                int green = (int) (
                    Math.sin(rainbowSpeed / 100000.0f * rainbowColourTick + 2.0f) * 127.0
                        + 128.0);
                int blue = (int) (
                    Math.sin(rainbowSpeed / 100000.0f * rainbowColourTick + 4.0f) * 127.0
                        + 128.0);
                renderColour = new Color(red, green, blue, 255);
            }

            if (!mc.thePlayer.isSpectator() && dynamicBow
                && mc.thePlayer.getHeldItem() != null) {
                ItemStack item = mc.thePlayer.getHeldItem();
                int useCount = mc.thePlayer.getItemInUseCount();
                if (dynamicBow
                    && mc.thePlayer.getHeldItem().getItem() == Items.bow) {
                    float bowExtension =
                        (item.getItem().getMaxItemUseDuration(item) - useCount) / 20.0f;
                    if (useCount == 0 || bowExtension > 1.0f) {
                        bowExtension = 1.0f;
                    }
                    renderGap =
                        gap + (int) ((1.0f - bowExtension) * (gap + 5) * 2.0f);
                }
            }

            if (!mc.gameSettings.showDebugInfo || visibleDebug) {
                switch (crosshairType) {
                    case CIRCLE:
                        drawCircleCrosshair(screenWidth, screenHeight, renderGap,
                            renderColour);
                        break;
                    case SQUARE:
                        drawSquareCrosshair(screenWidth, screenHeight, renderGap,
                            renderColour);
                        break;
                    case ARROW:
                        drawArrowCrosshair(screenWidth, screenHeight, renderGap, renderColour);
                        break;
                    case X:
                        drawXCrosshair(screenWidth, screenHeight, renderGap, renderColour);
                        break;
                    default:
                        drawCrossCrosshair(screenWidth, screenHeight, renderGap, renderColour);
                        break;
                }

                if (dot) {
                    CustomCrosshairGraphics
                        .drawFilledRectangle(screenWidth, screenHeight, screenWidth + 1,
                            screenHeight + 1, colourDot);
                }
            } else {
                drawDebugAxisCrosshair(screenWidth, screenHeight);
            }
        }
        GlStateManager.resetColor();
    }

    private void drawCrossCrosshair(int screenWidth, int screenHeight, int renderGap,
                                    Color renderColour) {
        int renderThickness = thickness / 2;
        if (outline) {
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness - 1,
                screenHeight - renderGap + 1, screenWidth - renderThickness,
                screenHeight - renderGap - height + 1, colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth + renderThickness + 1,
                screenHeight - renderGap + 1, screenWidth + renderThickness + 2,
                screenHeight - renderGap - height + 1, colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness - 1,
                screenHeight - renderGap + 2, screenWidth + renderThickness + 2,
                screenHeight - renderGap + 1, colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness - 1,
                screenHeight - renderGap - height, screenWidth + renderThickness + 2,
                screenHeight - renderGap - height + 1, colourOutline);
            CustomCrosshairGraphics
                .drawFilledRectangle(screenWidth - renderThickness - 1, screenHeight + renderGap,
                    screenWidth - renderThickness, screenHeight + renderGap + height + 1,
                    colourOutline);
            CustomCrosshairGraphics
                .drawFilledRectangle(screenWidth + renderThickness + 1, screenHeight + renderGap,
                    screenWidth + renderThickness + 2,
                    screenHeight + renderGap + height + 1, colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness - 1,
                screenHeight + renderGap - 1, screenWidth + renderThickness + 2,
                screenHeight + renderGap, colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness - 1,
                screenHeight + renderGap + height, screenWidth + renderThickness + 2,
                screenHeight + renderGap + height + 1, colourOutline);
            CustomCrosshairGraphics
                .drawFilledRectangle(screenWidth + renderGap, screenHeight - renderThickness - 1,
                    screenWidth + renderGap + width, screenHeight - renderThickness,
                    colourOutline);
            CustomCrosshairGraphics
                .drawFilledRectangle(screenWidth + renderGap, screenHeight + renderThickness + 1,
                    screenWidth + renderGap + width, screenHeight + renderThickness + 2,
                    colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth + renderGap - 1,
                screenHeight - renderThickness - 1, screenWidth + renderGap,
                screenHeight + renderThickness + 2, colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth + renderGap + width,
                screenHeight - renderThickness - 1, screenWidth + renderGap + width + 1,
                screenHeight + renderThickness + 2, colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderGap + 1,
                screenHeight - renderThickness - 1, screenWidth - renderGap - width,
                screenHeight - renderThickness, colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderGap + 1,
                screenHeight + renderThickness + 1, screenWidth - renderGap - width,
                screenHeight + renderThickness + 2, colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderGap + 2,
                screenHeight - renderThickness - 1, screenWidth - renderGap + 1,
                screenHeight + renderThickness + 2, colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderGap - width,
                screenHeight - renderThickness - 1, screenWidth - renderGap - width + 1,
                screenHeight + renderThickness + 2, colourOutline);
        }
        CustomCrosshairGraphics
            .drawFilledRectangle(screenWidth - renderThickness, screenHeight - renderGap + 1,
                screenWidth + renderThickness + 1, screenHeight - renderGap - height + 1,
                renderColour);
        CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness, screenHeight + renderGap,
            screenWidth + renderThickness + 1, screenHeight + renderGap + height,
            renderColour);
        CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderGap + 1,
            screenHeight - thickness / 2, screenWidth - renderGap - width + 1,
            screenHeight + renderThickness + 1, renderColour);
        CustomCrosshairGraphics
            .drawFilledRectangle(screenWidth + renderGap, screenHeight - thickness / 2,
                screenWidth + renderGap + width, screenHeight + renderThickness + 1,
                renderColour);
    }

    private void drawCircleCrosshair(int screenWidth, int screenHeight, int renderGap,
                                     Color renderColour) {
        if (outline) {
            int t = (thickness - thickness % 2) / 2 + 1;
            if (t > 3) {
                t = 3;
            }
            GL11.glLineWidth(2.0f);
            CustomCrosshairGraphics.drawCircle(screenWidth + 0.5, screenHeight + 0.5, renderGap - 1,
                colourOutline);
            CustomCrosshairGraphics
                .drawCircle(screenWidth + 0.5, screenHeight + 0.5, renderGap + t + 1,
                    colourOutline);
        }
        GL11.glLineWidth((float) (thickness + 1));
        CustomCrosshairGraphics
            .drawCircle(screenWidth + 0.5, screenHeight + 0.5, renderGap + 1, renderColour);
    }

    private void drawXCrosshair(int screenWidth, int screenHeight, int renderGap,
                                Color renderColour) {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_POINT_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        if (outline) {
            GL11.glLineWidth((float) (thickness + 6));
            CustomCrosshairGraphics
                .drawLine(screenWidth - width - 1, screenHeight + height + 1,
                    screenWidth, screenHeight, colourOutline);
            CustomCrosshairGraphics
                .drawLine(screenWidth, screenHeight, screenWidth + width + 1,
                    screenHeight + height + 1, colourOutline);
            CustomCrosshairGraphics
                .drawLine(screenWidth + width + 1, screenHeight - height - 1,
                    screenWidth, screenHeight, colourOutline);
            CustomCrosshairGraphics
                .drawLine(screenWidth, screenHeight, screenWidth - width - 1,
                    screenHeight - height - 1, colourOutline);
        }
        GL11.glLineWidth((float) (thickness + 1));
        CustomCrosshairGraphics
            .drawLine(screenWidth - width, screenHeight + height, screenWidth,
                screenHeight, renderColour);
        CustomCrosshairGraphics.drawLine(screenWidth, screenHeight, screenWidth + width,
            screenHeight + height, renderColour);
        CustomCrosshairGraphics
            .drawLine(screenWidth + width, screenHeight - height, screenWidth,
                screenHeight, renderColour);
        CustomCrosshairGraphics.drawLine(screenWidth, screenHeight, screenWidth - width,
            screenHeight - height, renderColour);
    }

    private void drawSquareCrosshair(int screenWidth, int screenHeight, int renderGap,
                                     Color renderColour) {
        if (outline) {
            CustomCrosshairGraphics
                .drawRectangle(screenWidth - renderGap - 1, screenHeight - renderGap - 1,
                    screenWidth + renderGap + 1, screenHeight + renderGap + 1, colourOutline);
            CustomCrosshairGraphics
                .drawRectangle(screenWidth - renderGap + 1, screenHeight - renderGap + 1,
                    screenWidth + renderGap - 1, screenHeight + renderGap - 1, colourOutline);
        }
        CustomCrosshairGraphics.drawRectangle(screenWidth - renderGap, screenHeight - renderGap,
            screenWidth + renderGap, screenHeight + renderGap, renderColour);
    }

    private void drawArrowCrosshair(int screenWidth, int screenHeight, int renderGap,
                                    Color renderColour) {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_POINT_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        if (outline) {
            GL11.glLineWidth((float) (thickness + 6));
            CustomCrosshairGraphics
                .drawLine(screenWidth - width - 1, screenHeight + height + 1,
                    screenWidth, screenHeight, colourOutline);
            CustomCrosshairGraphics
                .drawLine(screenWidth, screenHeight, screenWidth + width + 1,
                    screenHeight + height + 1, colourOutline);
        }
        GL11.glLineWidth((float) (thickness + 1));
        CustomCrosshairGraphics
            .drawLine(screenWidth - width, screenHeight + height, screenWidth,
                screenHeight, renderColour);
        CustomCrosshairGraphics.drawLine(screenWidth, screenHeight, screenWidth + width,
            screenHeight + height, renderColour);
    }

    private void drawDebugAxisCrosshair(int screenWidth, int screenHeight) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) screenWidth, (float) screenHeight, 0.0f);
        Entity entity = mc.getRenderViewEntity();
        GlStateManager.rotate(
            entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * 1.0f,
            -1.0f, 0.0f, 0.0f);
        GlStateManager
            .rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * 1.0f,
                0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-1.0f, -1.0f, -1.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_CLIENT_PIXEL_STORE_BIT, DefaultVertexFormats.POSITION_COLOR);
        GL11.glLineWidth(2.0f);
        worldrenderer.pos(0.0, 0.0, 0.0).color(255, 0, 0, 255).endVertex();
        worldrenderer.pos(10.0, 0.0, 0.0).color(255, 0, 0, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).color(0, 255, 0, 255).endVertex();
        worldrenderer.pos(0.0, 10.0, 0.0).color(0, 255, 0, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).color(0, 0, 255, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 10.0).color(0, 0, 255, 255).endVertex();
        tessellator.draw();
        GL11.glLineWidth(1.0f);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public CrosshairType getCrosshairType() {
        return crosshairType;
    }

    public void setCrosshairType(int crosshairTypeId) {
        switch (crosshairTypeId) {
            case 1: {
                crosshairType = CrosshairType.CIRCLE;
                break;
            }
            case 2: {
                crosshairType = CrosshairType.SQUARE;
                break;
            }
            case 3: {
                crosshairType = CrosshairType.ARROW;
                break;
            }
            case 4: {
                crosshairType = CrosshairType.X;
                break;
            }
            default: {
                crosshairType = CrosshairType.CROSS;
                break;
            }
        }
    }

    public int getCrosshairTypeID() {
        switch (crosshairType) {
            case CIRCLE: {
                return 1;
            }
            case SQUARE: {
                return 2;
            }
            case ARROW: {
                return 3;
            }
            case X: {
                return 4;
            }
            default: {
                return 0;
            }
        }
    }

    public void setCrosshairType(CrosshairType crosshairType) {
        this.crosshairType = crosshairType;
    }
    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        showVanillaCrosshair = false;
    }

    public boolean getVisibleHiddenGui() {
        return visibleHiddenGui;
    }
    public void setVisibleHiddenGui(boolean visible) {
        visibleHiddenGui = visible;
    }
    public boolean getVisibleDefault() {
        return visibleDefault;
    }
    public void setVisibleDefault(boolean visible) {
        visibleDefault = visible;
    }
    public boolean getVisibleDebug() {
        return visibleDebug;
    }
    public void setVisibleDebug(boolean visible) {
        visibleDebug = visible;
    }
    public boolean getVisibleSpectator() {
        return visibleSpectator;
    }
    public void setVisibleSpectator(boolean visible) {
        visibleSpectator = visible;
    }
    public boolean getVisibleThirdPerson() {
        return visibleThirdPerson;
    }
    public void setVisibleThirdPerson(boolean visible) {
        visibleThirdPerson = visible;
    }
    public boolean getOutline() {
        return outline;
    }
    public void setOutline(boolean outline) {
        this.outline = outline;
    }
    public Color getOutlineColour() {
        return colourOutline;
    }
    public void setOutlineColour(Color colour) {
        colourOutline = colour;
    }
    public boolean getDot() {
        return dot;
    }
    public void setDot(boolean dot) {
        this.dot = dot;
    }
    public Color getDotColour() {
        return colourDot;
    }
    public void setDotColour(Color colour) {
        colourDot = colour;
    }
    public Color getColour() {
        return colour;
    }
    public void setColour(Color colour) {
        this.colour = colour;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getGap() {
        return gap;
    }
    public void setGap(int gap) {
        this.gap = gap;
    }
    public int getThickness() {
        return thickness;
    }
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }
    public boolean getDynamicBow() {
        return dynamicBow;
    }
    public void setDynamicBow(boolean dynamicBow) {
        this.dynamicBow = dynamicBow;
    }
    public boolean getRainbowCrosshair() {
        return rainbowCrosshair;
    }

    public void setRainbowCrosshair(boolean rainbowCrosshair) {
        this.rainbowCrosshair = rainbowCrosshair;
        if (rainbowCrosshair) {
            rainbowColourTick = 0;
        }
    }

    public int getRainbowSpeed() {
        return rainbowSpeed;
    }

    public void setRainbowSpeed(int rainbowSpeed) {
        this.rainbowSpeed = rainbowSpeed;
    }

    public enum CrosshairType {
        CROSS,
        CIRCLE,
        SQUARE,
        ARROW,
        X
    }
}
