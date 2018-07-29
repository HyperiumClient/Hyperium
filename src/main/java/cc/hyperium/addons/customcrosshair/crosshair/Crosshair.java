package cc.hyperium.addons.customcrosshair.crosshair;

import cc.hyperium.addons.customcrosshair.main.CrosshairVersion;
import cc.hyperium.addons.customcrosshair.main.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.GuiGraphics;
import cc.hyperium.addons.customcrosshair.utils.RGBA;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class Crosshair {
    public static boolean showVanillaCrosshair = true;
    private boolean enabled;
    private RGBA colour;
    private CrosshairType crosshairType;
    private boolean visibleHiddenGui;
    private boolean visibleDefault;
    private boolean visibleDebug;
    private boolean visibleSpectator;
    private boolean visibleThirdPerson;
    private boolean outline;
    private RGBA colourOutline;
    private boolean dot;
    private RGBA colourDot;
    private int width;
    private int height;
    private int gap;
    private int thickness;
    private boolean dynamicBow;
    private boolean rainbowCrosshair;
    private int rainbowSpeed;
    private int rainbowColourTick;
    private Minecraft mc;

    public Crosshair() {
        this.mc = Minecraft.getMinecraft();
        this.setCrosshairType(CrosshairType.CROSS);
        this.setEnabled(false);
        this.setColour(new RGBA(255, 255, 255, 255));
        this.setVisibleDefault(true);
        this.setVisibleHiddenGui(true);
        this.setVisibleDebug(true);
        this.setVisibleSpectator(true);
        this.setVisibleThirdPerson(false);
        this.setOutline(true);
        this.setOutlineColour(new RGBA(0, 0, 0, 255));
        this.setDot(false);
        this.setDotColour(new RGBA(255, 255, 255, 255));
        this.setWidth(5);
        this.setHeight(5);
        this.setGap(3);
        this.setThickness(1);
        this.setDynamicBow(true);
        this.setRainbowCrosshair(false);
        this.setRainbowSpeed(500);
        this.rainbowColourTick = 0;
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent event) {
        if (this.mc.theWorld != null && this.mc.currentScreen == null && this.enabled) {
            CustomCrosshairAddon.getCrosshairMod().getCrosshair().drawCrosshair();
        }
    }

    public void drawCrosshair() {
        if (CustomCrosshairAddon.showMessage) {
            CrosshairVersion requestedLatestVersion = CustomCrosshairAddon.getCrosshairMod().sendLatestVersionGetRequest();
            if (requestedLatestVersion != null && !requestedLatestVersion.version.equals(CustomCrosshairAddon.VERSION)) {
                ChatComponentText text = new ChatComponentText("\u00a79[Custom Crosshair Addon] \u00a7rNew version available: " + requestedLatestVersion.version + ". ");
                ChatComponentText downloadText = new ChatComponentText("Click here to download.");
                ChatStyle downloadTextStlye = new ChatStyle();
                downloadTextStlye.setColor(EnumChatFormatting.AQUA);
                downloadTextStlye.setUnderlined(true);
                downloadTextStlye.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, requestedLatestVersion.downloadUrl));
                downloadText.setChatStyle(downloadTextStlye);
                text.appendSibling(downloadText);
                Minecraft.getMinecraft().thePlayer.addChatMessage(text);
            }
            CustomCrosshairAddon.showMessage = false;
        }
        int[] screenSize = GuiGraphics.getScreenSize();
        int screenWidth = screenSize[0] / 2;
        int screenHeight = screenSize[1] / 2;
        if (!this.getEnabled() && !this.mc.gameSettings.hideGUI) {
            if (this.mc.gameSettings.showDebugInfo) {
                this.drawDebugAxisCrosshair(screenWidth, screenHeight);
            }
        }
        if (this.getEnabled() && ((this.mc.gameSettings.thirdPersonView > 0 && this.getVisibleThirdPerson()) || this.mc.gameSettings.thirdPersonView <= 0) && ((this.mc.gameSettings.hideGUI && this.getVisibleHiddenGui()) || !this.mc.gameSettings.hideGUI) && ((this.mc.thePlayer.isSpectator() && this.getVisibleSpectator()) || !this.mc.thePlayer.isSpectator())) {
            if (!this.getVisibleDefault()) {
                return;
            }
            if (this.mc.gameSettings.hideGUI) {
                double[] screenSizeDouble = GuiGraphics.getScreenSizeDouble();
                GlStateManager.clear(256);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0, screenSizeDouble[0], screenSizeDouble[1], 0.0, 1000.0, 3000.0);
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            }
            ++this.rainbowColourTick;
            RGBA renderColour = this.getColour();
            int renderGap = this.getGap();
            if (this.getRainbowCrosshair()) {
                int red = (int)(Math.sin(this.rainbowSpeed / 100000.0f * this.rainbowColourTick + 0.0f) * 127.0 + 128.0);
                int green = (int)(Math.sin(this.rainbowSpeed / 100000.0f * this.rainbowColourTick + 2.0f) * 127.0 + 128.0);
                int blue = (int)(Math.sin(this.rainbowSpeed / 100000.0f * this.rainbowColourTick + 4.0f) * 127.0 + 128.0);
                renderColour = new RGBA(red, green, blue, 255);
            }
            if (!this.mc.thePlayer.isSpectator() && this.getDynamicBow() && this.mc.thePlayer.getHeldItem() != null) {
                ItemStack item = this.mc.thePlayer.getHeldItem();
                int useCount = this.mc.thePlayer.getItemInUseCount();
                if (this.getDynamicBow() && this.mc.thePlayer.getHeldItem().getItem() == Items.bow) {
                    float bowExtension = (item.getItem().getMaxItemUseDuration(item) - useCount) / 20.0f;
                    if (useCount == 0 || bowExtension > 1.0f) {
                        bowExtension = 1.0f;
                    }
                    renderGap = this.getGap() + (int)((1.0f - bowExtension) * (this.getGap() + 5) * 2.0f);
                }
            }
            if (!this.mc.gameSettings.showDebugInfo || (this.mc.gameSettings.showDebugInfo && this.getVisibleDebug())) {
                switch (this.getCrosshairType()) {
                    case CIRCLE: {
                        this.drawCircleCrosshair(screenWidth, screenHeight, renderGap, renderColour);
                        break;
                    }
                    case SQUARE: {
                        this.drawSquareCrosshair(screenWidth, screenHeight, renderGap, renderColour);
                        break;
                    }
                    case ARROW: {
                        this.drawArrowCrosshair(screenWidth, screenHeight, renderGap, renderColour);
                        break;
                    }
                    default: {
                        this.drawCrossCrosshair(screenWidth, screenHeight, renderGap, renderColour);
                        break;
                    }
                }
                if (this.getDot()) {
                    GuiGraphics.drawFilledRectangle(screenWidth, screenHeight, screenWidth + 1, screenHeight + 1, this.getDotColour());
                }
            }
            else {
                this.drawDebugAxisCrosshair(screenWidth, screenHeight);
            }
        }
    }

    private void drawCrossCrosshair(int screenWidth, int screenHeight, int renderGap, RGBA renderColour) {
        int renderThickness = this.getThickness() / 2;
        if (this.getOutline()) {
            GuiGraphics.drawFilledRectangle(screenWidth - renderThickness - 1, screenHeight - renderGap + 1, screenWidth - renderThickness, screenHeight - renderGap - this.getHeight() + 1, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth + renderThickness + 1, screenHeight - renderGap + 1, screenWidth + renderThickness + 2, screenHeight - renderGap - this.getHeight() + 1, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth - renderThickness - 1, screenHeight - renderGap + 2, screenWidth + renderThickness + 2, screenHeight - renderGap + 1, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth - renderThickness - 1, screenHeight - renderGap - this.getHeight(), screenWidth + renderThickness + 2, screenHeight - renderGap - this.getHeight() + 1, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth - renderThickness - 1, screenHeight + renderGap, screenWidth - renderThickness, screenHeight + renderGap + this.getHeight() + 1, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth + renderThickness + 1, screenHeight + renderGap, screenWidth + renderThickness + 2, screenHeight + renderGap + this.getHeight() + 1, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth - renderThickness - 1, screenHeight + renderGap - 1, screenWidth + renderThickness + 2, screenHeight + renderGap, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth - renderThickness - 1, screenHeight + renderGap + this.getHeight(), screenWidth + renderThickness + 2, screenHeight + renderGap + this.getHeight() + 1, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth + renderGap, screenHeight - renderThickness - 1, screenWidth + renderGap + this.getWidth(), screenHeight - renderThickness, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth + renderGap, screenHeight + renderThickness + 1, screenWidth + renderGap + this.getWidth(), screenHeight + renderThickness + 2, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth + renderGap - 1, screenHeight - renderThickness - 1, screenWidth + renderGap, screenHeight + renderThickness + 2, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth + renderGap + this.getWidth(), screenHeight - renderThickness - 1, screenWidth + renderGap + this.getWidth() + 1, screenHeight + renderThickness + 2, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth - renderGap + 1, screenHeight - renderThickness - 1, screenWidth - renderGap - this.getWidth(), screenHeight - renderThickness, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth - renderGap + 1, screenHeight + renderThickness + 1, screenWidth - renderGap - this.getWidth(), screenHeight + renderThickness + 2, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth - renderGap + 2, screenHeight - renderThickness - 1, screenWidth - renderGap + 1, screenHeight + renderThickness + 2, this.getOutlineColour());
            GuiGraphics.drawFilledRectangle(screenWidth - renderGap - this.getWidth(), screenHeight - renderThickness - 1, screenWidth - renderGap - this.getWidth() + 1, screenHeight + renderThickness + 2, this.getOutlineColour());
        }
        GuiGraphics.drawFilledRectangle(screenWidth - renderThickness, screenHeight - renderGap + 1, screenWidth + renderThickness + 1, screenHeight - renderGap - this.getHeight() + 1, renderColour);
        GuiGraphics.drawFilledRectangle(screenWidth - renderThickness, screenHeight + renderGap, screenWidth + renderThickness + 1, screenHeight + renderGap + this.getHeight(), renderColour);
        GuiGraphics.drawFilledRectangle(screenWidth - renderGap + 1, screenHeight - this.getThickness() / 2, screenWidth - renderGap - this.getWidth() + 1, screenHeight + renderThickness + 1, renderColour);
        GuiGraphics.drawFilledRectangle(screenWidth + renderGap, screenHeight - this.getThickness() / 2, screenWidth + renderGap + this.getWidth(), screenHeight + renderThickness + 1, renderColour);
    }

    private void drawCircleCrosshair(int screenWidth, int screenHeight, int renderGap, RGBA renderColour) {
        if (this.getOutline()) {
            int t = (this.getThickness() - this.getThickness() % 2) / 2 + 1;
            if (t > 3) {
                t = 3;
            }
            GL11.glLineWidth(2.0f);
            GuiGraphics.drawCircle(screenWidth + 0.5, screenHeight + 0.5, renderGap - 1, this.getOutlineColour());
            GuiGraphics.drawCircle(screenWidth + 0.5, screenHeight + 0.5, renderGap + t + 1, this.getOutlineColour());
        }
        GL11.glLineWidth((float)(this.getThickness() + 1));
        GuiGraphics.drawCircle(screenWidth + 0.5, screenHeight + 0.5, renderGap + 1, renderColour);
    }

    private void drawSquareCrosshair(int screenWidth, int screenHeight, int renderGap, RGBA renderColour) {
        if (this.getOutline()) {
            GuiGraphics.drawRectangle(screenWidth - renderGap - 1, screenHeight - renderGap - 1, screenWidth + renderGap + 1, screenHeight + renderGap + 1, this.getOutlineColour());
            GuiGraphics.drawRectangle(screenWidth - renderGap + 1, screenHeight - renderGap + 1, screenWidth + renderGap - 1, screenHeight + renderGap - 1, this.getOutlineColour());
        }
        GuiGraphics.drawRectangle(screenWidth - renderGap, screenHeight - renderGap, screenWidth + renderGap, screenHeight + renderGap, renderColour);
    }

    private void drawArrowCrosshair(int screenWidth, int screenHeight, int renderGap, RGBA renderColour) {
        GL11.glDisable(2848);
        GL11.glDisable(2832);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        if (this.getOutline()) {
            GL11.glLineWidth((float)(this.getThickness() + 6));
            GuiGraphics.drawLine(screenWidth - this.getWidth() - 1, screenHeight + this.getHeight() + 1, screenWidth, screenHeight, this.getOutlineColour());
            GuiGraphics.drawLine(screenWidth, screenHeight, screenWidth + this.getWidth() + 1, screenHeight + this.getHeight() + 1, this.getOutlineColour());
        }
        GL11.glLineWidth((float)(this.getThickness() + 1));
        GuiGraphics.drawLine(screenWidth - this.getWidth(), screenHeight + this.getHeight(), screenWidth, screenHeight, renderColour);
        GuiGraphics.drawLine(screenWidth, screenHeight, screenWidth + this.getWidth(), screenHeight + this.getHeight(), renderColour);
    }

    private void drawDebugAxisCrosshair(int screenWidth, int screenHeight) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)screenWidth, (float)screenHeight, 0.0f);
        Entity entity = this.mc.getRenderViewEntity();
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * 1.0f, -1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * 1.0f, 0.0f, 1.0f, 0.0f);
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

    public int getCrosshairTypeID() {
        switch (this.getCrosshairType()) {
            case CIRCLE: {
                return 1;
            }
            case SQUARE: {
                return 2;
            }
            case ARROW: {
                return 3;
            }
            default: {
                return 0;
            }
        }
    }

    public String getCrosshairTypeString() {
        switch (this.getCrosshairType()) {
            case CIRCLE: {
                return "CIRCLE";
            }
            case SQUARE: {
                return "SQUARE";
            }
            case ARROW: {
                return "ARROW";
            }
            default: {
                return "CROSS";
            }
        }
    }

    public void setCrosshairType(CrosshairType crosshairType) {
        this.crosshairType = crosshairType;
    }

    public void setCrosshairType(int crosshairTypeId) {
        switch (crosshairTypeId) {
            case 1: {
                this.setCrosshairType(CrosshairType.CIRCLE);
                break;
            }
            case 2: {
                this.setCrosshairType(CrosshairType.SQUARE);
                break;
            }
            case 3: {
                this.setCrosshairType(CrosshairType.ARROW);
                break;
            }
            default: {
                this.setCrosshairType(CrosshairType.CROSS);
                break;
            }
        }
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

    public RGBA getOutlineColour() {
        return this.colourOutline;
    }

    public void setOutlineColour(RGBA colour) {
        this.colourOutline = colour;
    }

    public boolean getDot() {
        return this.dot;
    }

    public void setDot(boolean dot) {
        this.dot = dot;
    }

    public RGBA getDotColour() {
        return this.colourDot;
    }

    public void setDotColour(RGBA colour) {
        this.colourDot = colour;
    }

    public RGBA getColour() {
        return this.colour;
    }

    public void setColour(RGBA colour) {
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

    public enum CrosshairType
    {
        CROSS,
        CIRCLE,
        SQUARE,
        ARROW
    }
}
