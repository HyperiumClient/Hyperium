package cc.hyperium.mixins.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Random;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
    @Shadow
    @Final
    private static ResourceLocation[] unicodePageLocations;
    @Shadow
    public int FONT_HEIGHT;
    @Shadow
    public Random fontRandom;
    @Shadow
    private int[] charWidth;
    @Shadow
    private byte[] glyphWidth;
    @Shadow
    private int[] colorCode;
    @Shadow
    @Final
    private ResourceLocation locationFontTexture;
    @Shadow
    @Final
    private TextureManager renderEngine;
    @Shadow
    private float posX;
    @Shadow
    private float posY;
    @Shadow
    private boolean unicodeFlag;
    @Shadow
    private boolean bidiFlag;
    @Shadow
    private float red;
    @Shadow
    private float blue;
    @Shadow
    private float green;
    @Shadow
    private float alpha;
    @Shadow
    private int textColor;
    @Shadow
    private boolean randomStyle;
    @Shadow
    private boolean boldStyle;
    @Shadow
    private boolean italicStyle;
    @Shadow
    private boolean underlineStyle;
    @Shadow
    private boolean strikethroughStyle;
    private HashMap<String, DynamicTexture> textureCache = new HashMap<>();
    private HashMap<String, DynamicTexture> obfuscatedCache = new HashMap<>();

    @Shadow
    protected abstract void resetStyles();

    @Shadow
    protected abstract String bidiReorder(String text);

    @Shadow
    protected abstract void renderStringAtPos(String text, boolean shadow);

    @Overwrite
    private int renderString(String text, float x, float y, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        } else {
            if (this.bidiFlag) {
                text = this.bidiReorder(text);
            }

            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (dropShadow) {
                color = (color & 16579836) >> 2 | color & -16777216;
            }

            this.red = (float) (color >> 16 & 255) / 255.0F;
            this.blue = (float) (color >> 8 & 255) / 255.0F;
            this.green = (float) (color & 255) / 255.0F;
            this.alpha = (float) (color >> 24 & 255) / 255.0F;
            GlStateManager.color(this.red, this.blue, this.green, this.alpha);
            this.posX = x;
            this.posY = y;
            this.renderStringAtPos(text, dropShadow);
            return (int) this.posX;
        }
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        float zLevel = /*currentScreen != null ? currentScreen.zLevel :*/ 0;
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double) (x + 0), (double) (y + height), (double) zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y + height), (double) zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y + 0), (double) zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
        worldrenderer.pos((double) (x + 0), (double) (y + 0), (double) zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }

    private HashMap<DynamicTexture, Integer> returnCache = new HashMap<>();
    @Overwrite
    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        DynamicTexture texture = textureCache.get(text);
        if (texture != null) {
            GlStateManager.bindTexture(texture.getGlTextureId());
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(256 + 15, 0, 0);
            drawTexturedModalRect(0, 0, 0, 0, 15, 256);
            return returnCache.get(texture);
        }

        GlStateManager.enableAlpha();
        this.resetStyles();
        int i;

        if (dropShadow) {
            i = this.renderString(text, x + 1.0F, y + 1.0F, color, true);
            i = Math.max(i, this.renderString(text, x, y, color, false));
        } else {
            i = this.renderString(text, x, y, color, false);
        }

        return i;
    }
}
