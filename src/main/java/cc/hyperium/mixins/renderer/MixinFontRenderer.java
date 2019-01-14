package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.FontRendererData;
import cc.hyperium.mixinsimp.client.GlStateModifier;
import cc.hyperium.mixinsimp.renderer.CachedString;
import cc.hyperium.mixinsimp.renderer.FontFixValues;
import cc.hyperium.mixinsimp.renderer.StringHash;
import cc.hyperium.mods.nickhider.NickHider;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Locale;
import java.util.Random;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @Shadow
    public int FONT_HEIGHT;

    @Shadow
    public Random fontRandom;

    @Shadow
    private int[] colorCode;

    @Shadow
    private float posX;

    @Shadow
    private float posY;

    @Shadow
    private float red;

    @Shadow
    private float blue;

    @Shadow
    private float green;

    @Shadow
    private float alpha;

    @Shadow
    private boolean boldStyle;

    @Shadow
    private boolean unicodeFlag;

    @Shadow
    private int textColor;

    @Shadow
    private boolean randomStyle;

    @Shadow
    private boolean italicStyle;

    @Shadow
    private boolean underlineStyle;

    @Shadow
    private boolean strikethroughStyle;

    @Shadow
    public abstract int getCharWidth(char character);

    @Shadow
    protected abstract float func_181559_a(char ch, boolean italic);

    @Shadow
    public abstract int drawString(String text, int x, int y, int color);

    @Shadow
    public abstract int drawString(String text, float x, float y, int color, boolean dropShadow);


    public void setColor(float red, float g, float b, float a) {
        GlStateManager.color(red, g, b, a);
    }

    /**
     * @author Sk1er
     * @reason Optimized Font Renderer
     */
    @Overwrite
    private void renderStringAtPos(String text, boolean shadow) {
        //Full speed ahead
        //Should help fix issues
        GlStateModifier.INSTANCE.reset();
        boolean optimize = Settings.OPTIMIZED_FONT_RENDERER;

        if (FontFixValues.INSTANCE == null) {
            FontFixValues.INSTANCE = new FontFixValues();
        }

        FontFixValues instance = FontFixValues.INSTANCE;

        int list = 0;
        final float posX = this.posX;
        final float posY = this.posY;
        this.posY = 0;
        this.posX = 0;
        StringHash hash = new StringHash(text, red, green, blue, alpha, shadow);
        GlStateManager.translate(posX, posY, 0F);
        if (optimize) {
            CachedString cachedString = instance.get(hash);
            if (cachedString != null) {
                GlStateManager.color(this.red, this.blue, this.green, alpha);

                GlStateManager.callList(cachedString.getListId());
                //Call so states in game know the texture was changed. Otherwise the game won't know the active texture was changed on the GPU
                GlStateModifier.INSTANCE.reset();
                GlStateManager.color(cachedString.getLastRed(), cachedString.getLastGreen(), cachedString.getLastBlue(), cachedString.getLastAlpha());
                GlStateManager.translate(-posX, -posY, 0F);
                this.posY = posY + cachedString.getHeight();
                this.posX = posX + cachedString.getWidth();
                return;
            }
            list = GLAllocation.generateDisplayLists(1);

            GL11.glNewList(list, GL11.GL_COMPILE_AND_EXECUTE);
        }

        boolean hasObf = false;
        CachedString value = new CachedString(text, list, this.posX - posX, this.posY - posY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.0F);
        this.func_181559_a('.', this.italicStyle);
        GlStateManager.color(red, blue, green, alpha);

        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);

            if (c0 == 167 && i + 1 < text.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (i1 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;

                    if (i1 < 0 || i1 > 15) {
                        i1 = 15;
                    }

                    if (shadow) {
                        i1 += 16;
                    }

                    int j1 = this.colorCode[i1];
                    this.textColor = j1;
                    float red = (float) (j1 >> 16) / 255.0F;
                    float green = (float) (j1 >> 8 & 255) / 255.0F;
                    float blue = (float) (j1 & 255) / 255.0F;
                    setColor(red, green, blue, alpha);
                    value.setLastRed(red);
                    value.setLastBlue(blue);
                    value.setLastGreen(green);
                    value.setLastAlpha(alpha);
                } else if (i1 == 16) {
                    this.randomStyle = true;
                    hasObf = true;
                } else if (i1 == 17) {
                    this.boldStyle = true;
                } else if (i1 == 18) {
                    this.strikethroughStyle = true;
                } else if (i1 == 19) {
                    this.underlineStyle = true;
                } else if (i1 == 20) {
                    this.italicStyle = true;
                } else if (i1 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    setColor(this.red, this.blue, this.green, alpha);
                    value.setLastRed(this.red);
                    value.setLastBlue(this.blue);
                    value.setLastGreen(this.green);
                    value.setLastAlpha(alpha);
                }

                ++i;
            } else {
                int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);

                if (this.randomStyle && j != -1) {
                    int k = this.getCharWidth(c0);
                    char c1;

                    while (true) {
                        j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
                        c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j);

                        if (k == this.getCharWidth(c1)) {
                            break;
                        }
                    }

                    c0 = c1;
                }

                float f1 = j == -1 || this.unicodeFlag ? 0.5f : 1f;
                boolean flag = (c0 == 0 || j == -1 || this.unicodeFlag) && shadow;

                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f = this.func_181559_a(c0, this.italicStyle);

                if (flag) {
                    this.posX += f1;
                    this.posY += f1;
                }

                if (this.boldStyle) {
                    this.posX += f1;

                    if (flag) {
                        this.posX -= f1;
                        this.posY -= f1;
                    }

                    this.func_181559_a(c0, this.italicStyle);
                    this.posX -= f1;

                    if (flag) {
                        this.posX += f1;
                        this.posY += f1;
                    }

                    ++f;
                }
                doDraw(f);
            }
        }
        if (optimize) {
            GL11.glEndList();
            instance.cache(hash, value);
        }
        value.setWidth(this.posX);
        this.posY = posY + value.getHeight();
        this.posX = posX + value.getWidth();
        if (hasObf) {
            instance.obfuscated.add(hash);
        }
        GlStateManager.translate(-posX, -posY, 0F);

    }

    protected void doDraw(float f) {
        {
            {

                if (this.strikethroughStyle) {
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                    worldrenderer.pos((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D).endVertex();
                    worldrenderer.pos((double) (this.posX + f), (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D).endVertex();
                    worldrenderer.pos((double) (this.posX + f), (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
                    worldrenderer.pos((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }

                if (this.underlineStyle) {
                    Tessellator tessellator1 = Tessellator.getInstance();
                    WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
                    int l = this.underlineStyle ? -1 : 0;
                    worldrenderer1.pos((double) (this.posX + (float) l), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D).endVertex();
                    worldrenderer1.pos((double) (this.posX + f), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D).endVertex();
                    worldrenderer1.pos((double) (this.posX + f), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
                    worldrenderer1.pos((double) (this.posX + (float) l), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
                    tessellator1.draw();
                    GlStateManager.enableTexture2D();
                }

                this.posX += (float) ((int) f);
            }
        }
    }

    @ModifyVariable(method = "renderString", at = @At(value = "HEAD"))
    public String mod(String in) {
        if (NickHider.INSTANCE == null) { // When mod hasn't initialized yet.
            return in;
        }
        return NickHider.INSTANCE.apply(in);
    }

    @ModifyVariable(method = "getStringWidth", at = @At(value = "HEAD"))
    public String modWidth(String in) {
        if (NickHider.INSTANCE == null) { // When mod hasn't initialized yet.
            return in;
        }
        return NickHider.INSTANCE.apply(in);
    }

    /**
     * @author Sk1er
     * @reason Optimized Font Renderer
     */
    @Overwrite
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        } else {

            HyperiumHandlers handlers = Hyperium.INSTANCE.getHandlers();
            if (handlers != null)
                if (FontRendererData.INSTANCE.stringWidthCache.size() > handlers.getConfigOptions().stringCacheSize)
                    FontRendererData.INSTANCE.stringWidthCache.clear();
            return FontRendererData.INSTANCE.stringWidthCache.computeIfAbsent(text, (text1) -> {
                int i = 0;
                boolean flag = false;

                for (int j = 0; j < text.length(); ++j) {
                    char c0 = text.charAt(j);
                    int k = this.getCharWidth(c0);
                    if (k < 0 && j < text.length() - 1) {
                        ++j;
                        c0 = text.charAt(j);

                        if (c0 != 108 && c0 != 76) {
                            if (c0 == 114 || c0 == 82) {
                                flag = false;
                            }
                        } else {
                            flag = true;
                        }

                        k = 0;
                    }
                    i += k;
                    if (flag && k > 0) {
                        ++i;
                    }
                }
                return i;
            });

        }
    }
}
