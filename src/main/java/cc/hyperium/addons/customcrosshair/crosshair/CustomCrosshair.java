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
import cc.hyperium.event.RenderHUDEvent;
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
        this.mc = Minecraft.getMinecraft();
        this.crosshairType = CrosshairType.CROSS;
        this.setEnabled(false);
        this.colour = new Color(255, 255, 255, 255);
        this.visibleDefault = true;
        this.visibleHiddenGui = true;
        this.visibleDebug = true;
        this.visibleSpectator = true;
        this.visibleThirdPerson = false;
        this.outline = true;
        this.colourOutline = new Color(0, 0, 0, 255);
        this.dot = false;
        this.colourDot = new Color(255, 255, 255, 255);
        this.width = 5;
        this.height = 5;
        this.gap = 3;
        this.thickness = 1;
        this.dynamicBow = true;
        this.setRainbowCrosshair(false);
        this.rainbowSpeed = 500;
        this.rainbowColourTick = 0;
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent event) {
        if (this.mc.theWorld != null && this.mc.currentScreen == null && this.enabled) {
            CustomCrosshairAddon.getCrosshairMod().getCrosshair().drawCrosshair();
        }
    }

    public void drawCrosshair() {
        int screenWidth = ResolutionUtil.current().getScaledWidth() / 2;
        int screenHeight = ResolutionUtil.current().getScaledHeight() / 2;
        if (!this.enabled && !this.mc.gameSettings.hideGUI) {
            if (this.mc.gameSettings.showDebugInfo) {
                this.drawDebugAxisCrosshair(screenWidth, screenHeight);
            }
        }
        if (this.enabled && (this.mc.gameSettings.thirdPersonView <= 0 || this.visibleThirdPerson) && (!this.mc.gameSettings.hideGUI || this.visibleHiddenGui) && (
            !this.mc.thePlayer
                .isSpectator() || this.visibleSpectator)) {
            if (!this.visibleDefault) {
                return;
            }
            if (this.mc.gameSettings.hideGUI) {
                GlStateManager.clear(GL11.GL_ACCUM);
                GlStateManager.matrixMode(GL11.GL_PROJECTION);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0, ResolutionUtil.current().getScaledWidth_double(),
                    ResolutionUtil.current().getScaledHeight_double(), 0.0, 1000.0, 3000.0);
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            }
            ++this.rainbowColourTick;
            Color renderColour = this.colour;
            int renderGap = this.gap;
            if (this.rainbowCrosshair) {
                int red = (int) (
                    Math.sin(this.rainbowSpeed / 100000.0f * this.rainbowColourTick + 0.0f) * 127.0
                        + 128.0);
                int green = (int) (
                    Math.sin(this.rainbowSpeed / 100000.0f * this.rainbowColourTick + 2.0f) * 127.0
                        + 128.0);
                int blue = (int) (
                    Math.sin(this.rainbowSpeed / 100000.0f * this.rainbowColourTick + 4.0f) * 127.0
                        + 128.0);
                renderColour = new Color(red, green, blue, 255);
            }

            if (!this.mc.thePlayer.isSpectator() && this.dynamicBow
                && this.mc.thePlayer.getHeldItem() != null) {
                ItemStack item = this.mc.thePlayer.getHeldItem();
                int useCount = this.mc.thePlayer.getItemInUseCount();
                if (this.dynamicBow
                    && this.mc.thePlayer.getHeldItem().getItem() == Items.bow) {
                    float bowExtension =
                        (item.getItem().getMaxItemUseDuration(item) - useCount) / 20.0f;
                    if (useCount == 0 || bowExtension > 1.0f) {
                        bowExtension = 1.0f;
                    }
                    renderGap =
                        this.gap + (int) ((1.0f - bowExtension) * (this.gap + 5) * 2.0f);
                }
            }

            if (!this.mc.gameSettings.showDebugInfo || this.visibleDebug) {
                switch (this.crosshairType) {
                    case CIRCLE:
                        this.drawCircleCrosshair(screenWidth, screenHeight, renderGap,
                            renderColour);
                        break;
                    case SQUARE:
                        this.drawSquareCrosshair(screenWidth, screenHeight, renderGap,
                            renderColour);
                        break;
                    case ARROW:
                        this.drawArrowCrosshair(screenWidth, screenHeight, renderGap, renderColour);
                        break;
                    case X:
                        this.drawXCrosshair(screenWidth, screenHeight, renderGap, renderColour);
                        break;
                    default:
                        this.drawCrossCrosshair(screenWidth, screenHeight, renderGap, renderColour);
                        break;
                }

                if (this.dot) {
                    CustomCrosshairGraphics
                        .drawFilledRectangle(screenWidth, screenHeight, screenWidth + 1,
                            screenHeight + 1, this.colourDot);
                }
            } else {
                this.drawDebugAxisCrosshair(screenWidth, screenHeight);
            }
        }
        GlStateManager.resetColor();
    }

    private void drawCrossCrosshair(int screenWidth, int screenHeight, int renderGap,
                                    Color renderColour) {
        int renderThickness = this.thickness / 2;
        if (this.outline) {
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness - 1,
                screenHeight - renderGap + 1, screenWidth - renderThickness,
                screenHeight - renderGap - this.height + 1, this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth + renderThickness + 1,
                screenHeight - renderGap + 1, screenWidth + renderThickness + 2,
                screenHeight - renderGap - this.height + 1, this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness - 1,
                screenHeight - renderGap + 2, screenWidth + renderThickness + 2,
                screenHeight - renderGap + 1, this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness - 1,
                screenHeight - renderGap - this.height, screenWidth + renderThickness + 2,
                screenHeight - renderGap - this.height + 1, this.colourOutline);
            CustomCrosshairGraphics
                .drawFilledRectangle(screenWidth - renderThickness - 1, screenHeight + renderGap,
                    screenWidth - renderThickness, screenHeight + renderGap + this.height + 1,
                    this.colourOutline);
            CustomCrosshairGraphics
                .drawFilledRectangle(screenWidth + renderThickness + 1, screenHeight + renderGap,
                    screenWidth + renderThickness + 2,
                    screenHeight + renderGap + this.height + 1, this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness - 1,
                screenHeight + renderGap - 1, screenWidth + renderThickness + 2,
                screenHeight + renderGap, this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness - 1,
                screenHeight + renderGap + this.height, screenWidth + renderThickness + 2,
                screenHeight + renderGap + this.height + 1, this.colourOutline);
            CustomCrosshairGraphics
                .drawFilledRectangle(screenWidth + renderGap, screenHeight - renderThickness - 1,
                    screenWidth + renderGap + this.width, screenHeight - renderThickness,
                    this.colourOutline);
            CustomCrosshairGraphics
                .drawFilledRectangle(screenWidth + renderGap, screenHeight + renderThickness + 1,
                    screenWidth + renderGap + this.width, screenHeight + renderThickness + 2,
                    this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth + renderGap - 1,
                screenHeight - renderThickness - 1, screenWidth + renderGap,
                screenHeight + renderThickness + 2, this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth + renderGap + this.width,
                screenHeight - renderThickness - 1, screenWidth + renderGap + this.width + 1,
                screenHeight + renderThickness + 2, this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderGap + 1,
                screenHeight - renderThickness - 1, screenWidth - renderGap - this.width,
                screenHeight - renderThickness, this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderGap + 1,
                screenHeight + renderThickness + 1, screenWidth - renderGap - this.width,
                screenHeight + renderThickness + 2, this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderGap + 2,
                screenHeight - renderThickness - 1, screenWidth - renderGap + 1,
                screenHeight + renderThickness + 2, this.colourOutline);
            CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderGap - this.width,
                screenHeight - renderThickness - 1, screenWidth - renderGap - this.width + 1,
                screenHeight + renderThickness + 2, this.colourOutline);
        }
        CustomCrosshairGraphics
            .drawFilledRectangle(screenWidth - renderThickness, screenHeight - renderGap + 1,
                screenWidth + renderThickness + 1, screenHeight - renderGap - this.height + 1,
                renderColour);
        CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderThickness, screenHeight + renderGap,
            screenWidth + renderThickness + 1, screenHeight + renderGap + this.height,
            renderColour);
        CustomCrosshairGraphics.drawFilledRectangle(screenWidth - renderGap + 1,
            screenHeight - this.thickness / 2, screenWidth - renderGap - this.width + 1,
            screenHeight + renderThickness + 1, renderColour);
        CustomCrosshairGraphics
            .drawFilledRectangle(screenWidth + renderGap, screenHeight - this.thickness / 2,
                screenWidth + renderGap + this.width, screenHeight + renderThickness + 1,
                renderColour);
    }

    private void drawCircleCrosshair(int screenWidth, int screenHeight, int renderGap,
                                     Color renderColour) {
        if (this.outline) {
            int t = (this.thickness - this.thickness % 2) / 2 + 1;
            if (t > 3) {
                t = 3;
            }
            GL11.glLineWidth(2.0f);
            CustomCrosshairGraphics.drawCircle(screenWidth + 0.5, screenHeight + 0.5, renderGap - 1,
                this.colourOutline);
            CustomCrosshairGraphics
                .drawCircle(screenWidth + 0.5, screenHeight + 0.5, renderGap + t + 1,
                    this.colourOutline);
        }
        GL11.glLineWidth((float) (this.thickness + 1));
        CustomCrosshairGraphics
            .drawCircle(screenWidth + 0.5, screenHeight + 0.5, renderGap + 1, renderColour);
    }

    private void drawXCrosshair(int screenWidth, int screenHeight, int renderGap,
                                Color renderColour) {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_POINT_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        if (this.outline) {
            GL11.glLineWidth((float) (this.thickness + 6));
            CustomCrosshairGraphics
                .drawLine(screenWidth - this.width - 1, screenHeight + this.height + 1,
                    screenWidth, screenHeight, this.colourOutline);
            CustomCrosshairGraphics
                .drawLine(screenWidth, screenHeight, screenWidth + this.width + 1,
                    screenHeight + this.height + 1, this.colourOutline);
            CustomCrosshairGraphics
                .drawLine(screenWidth + this.width + 1, screenHeight - this.height - 1,
                    screenWidth, screenHeight, this.colourOutline);
            CustomCrosshairGraphics
                .drawLine(screenWidth, screenHeight, screenWidth - this.width - 1,
                    screenHeight - this.height - 1, this.colourOutline);
        }
        GL11.glLineWidth((float) (this.thickness + 1));
        CustomCrosshairGraphics
            .drawLine(screenWidth - this.width, screenHeight + this.height, screenWidth,
                screenHeight, renderColour);
        CustomCrosshairGraphics.drawLine(screenWidth, screenHeight, screenWidth + this.width,
            screenHeight + this.height, renderColour);
        CustomCrosshairGraphics
            .drawLine(screenWidth + this.width, screenHeight - this.height, screenWidth,
                screenHeight, renderColour);
        CustomCrosshairGraphics.drawLine(screenWidth, screenHeight, screenWidth - this.width,
            screenHeight - this.height, renderColour);
    }

    private void drawSquareCrosshair(int screenWidth, int screenHeight, int renderGap,
                                     Color renderColour) {
        if (this.outline) {
            CustomCrosshairGraphics
                .drawRectangle(screenWidth - renderGap - 1, screenHeight - renderGap - 1,
                    screenWidth + renderGap + 1, screenHeight + renderGap + 1, this.colourOutline);
            CustomCrosshairGraphics
                .drawRectangle(screenWidth - renderGap + 1, screenHeight - renderGap + 1,
                    screenWidth + renderGap - 1, screenHeight + renderGap - 1, this.colourOutline);
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
        if (this.outline) {
            GL11.glLineWidth((float) (this.thickness + 6));
            CustomCrosshairGraphics
                .drawLine(screenWidth - this.width - 1, screenHeight + this.height + 1,
                    screenWidth, screenHeight, this.colourOutline);
            CustomCrosshairGraphics
                .drawLine(screenWidth, screenHeight, screenWidth + this.width + 1,
                    screenHeight + this.height + 1, this.colourOutline);
        }
        GL11.glLineWidth((float) (this.thickness + 1));
        CustomCrosshairGraphics
            .drawLine(screenWidth - this.width, screenHeight + this.height, screenWidth,
                screenHeight, renderColour);
        CustomCrosshairGraphics.drawLine(screenWidth, screenHeight, screenWidth + this.width,
            screenHeight + this.height, renderColour);
    }

    private void drawDebugAxisCrosshair(int screenWidth, int screenHeight) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) screenWidth, (float) screenHeight, 0.0f);
        Entity entity = this.mc.getRenderViewEntity();
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
        worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
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
        return this.crosshairType;
    }

    public void setCrosshairType(int crosshairTypeId) {
        switch (crosshairTypeId) {
            case 1: {
                this.crosshairType = CrosshairType.CIRCLE;
                break;
            }
            case 2: {
                this.crosshairType = CrosshairType.SQUARE;
                break;
            }
            case 3: {
                this.crosshairType = CrosshairType.ARROW;
                break;
            }
            case 4: {
                this.crosshairType = CrosshairType.X;
                break;
            }
            default: {
                this.crosshairType = CrosshairType.CROSS;
                break;
            }
        }
    }

    public int getCrosshairTypeID() {
        switch (this.crosshairType) {
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
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        showVanillaCrosshair = false;
    }

    public boolean getVisibleHiddenGui() {
        return this.visibleHiddenGui;
    }
    public void setVisibleHiddenGui(boolean visible) {
        this.visibleHiddenGui = visible;
    }
    public boolean getVisibleDefault() {
        return this.visibleDefault;
    }
    public void setVisibleDefault(boolean visible) {
        this.visibleDefault = visible;
    }
    public boolean getVisibleDebug() {
        return this.visibleDebug;
    }
    public void setVisibleDebug(boolean visible) {
        this.visibleDebug = visible;
    }
    public boolean getVisibleSpectator() {
        return this.visibleSpectator;
    }
    public void setVisibleSpectator(boolean visible) {
        this.visibleSpectator = visible;
    }
    public boolean getVisibleThirdPerson() {
        return this.visibleThirdPerson;
    }
    public void setVisibleThirdPerson(boolean visible) {
        this.visibleThirdPerson = visible;
    }
    public boolean getOutline() {
        return this.outline;
    }
    public void setOutline(boolean outline) {
        this.outline = outline;
    }
    public Color getOutlineColour() {
        return this.colourOutline;
    }
    public void setOutlineColour(Color colour) {
        this.colourOutline = colour;
    }
    public boolean getDot() {
        return this.dot;
    }
    public void setDot(boolean dot) {
        this.dot = dot;
    }
    public Color getDotColour() {
        return this.colourDot;
    }
    public void setDotColour(Color colour) {
        this.colourDot = colour;
    }
    public Color getColour() {
        return this.colour;
    }
    public void setColour(Color colour) {
        this.colour = colour;
    }
    public int getWidth() {
        return this.width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getGap() {
        return this.gap;
    }
    public void setGap(int gap) {
        this.gap = gap;
    }
    public int getThickness() {
        return this.thickness;
    }
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }
    public boolean getDynamicBow() {
        return this.dynamicBow;
    }
    public void setDynamicBow(boolean dynamicBow) {
        this.dynamicBow = dynamicBow;
    }
    public boolean getRainbowCrosshair() {
        return this.rainbowCrosshair;
    }

    public void setRainbowCrosshair(boolean rainbowCrosshair) {
        this.rainbowCrosshair = rainbowCrosshair;
        if (rainbowCrosshair) {
            this.rainbowColourTick = 0;
        }
    }

    public int getRainbowSpeed() {
        return this.rainbowSpeed;
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
