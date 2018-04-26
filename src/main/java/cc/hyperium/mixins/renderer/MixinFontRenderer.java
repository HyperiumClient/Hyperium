package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.FontRendererData;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {


    /**
     * @author Sk1er
     */
//    @Overwrite
//    private void renderStringAtPos(String text, boolean shadow) {
//        int list = 0;
//        float posX = this.posX;
//        float posY = this.posY;
//        this.posY = 0;
//        this.posX = 0;
//        GlStateManager.translate(posX, posY, 0F);
//        if (Hyperium.INSTANCE.getHandlers().getConfigOptions().alternateFontRenderer && text.length() > 10) {
//            CachedString cachedString = shadow ? FontRendererData.INSTANCE.shadowStringCache.get(text) : FontRendererData.INSTANCE.normalStringCache.get(text);
//            if (cachedString != null) {
//                GlStateManager.callList(cachedString.getListId());
//                this.renderEngine.bindTexture(this.locationFontTexture);
//                GlStateManager.translate(-posX, -posY, 0F);
//                this.posY = posY + cachedString.getHeight();
//                this.posX = posX + cachedString.getWidth();
//                return;
//            }
//
//            list = GLAllocation.generateDisplayLists(1);
//            GL11.glNewList(list, GL11.GL_COMPILE_AND_EXECUTE);
//        }
//        for (int i = 0; i < text.length(); ++i) {
//            char c0 = text.charAt(i);
//
//            if (c0 == 167 && i + 1 < text.length()) {
//                int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
//
//                if (i1 < 16) {
//                    this.randomStyle = false;
//                    this.boldStyle = false;
//                    this.strikethroughStyle = false;
//                    this.underlineStyle = false;
//                    this.italicStyle = false;
//
//                    if (i1 < 0 || i1 > 15) {
//                        i1 = 15;
//                    }
//
//                    if (shadow) {
//                        i1 += 16;
//                    }
//
//                    int j1 = this.colorCode[i1];
//                    this.textColor = j1;
//                    GlStateManager.color((float) (j1 >> 16) / 255.0F, (float) (j1 >> 8 & 255) / 255.0F, (float) (j1 & 255) / 255.0F, this.alpha);
//                } else if (i1 == 16) {
//                    this.randomStyle = true;
//                } else if (i1 == 17) {
//                    this.boldStyle = true;
//                } else if (i1 == 18) {
//                    this.strikethroughStyle = true;
//                } else if (i1 == 19) {
//                    this.underlineStyle = true;
//                } else if (i1 == 20) {
//                    this.italicStyle = true;
//                } else if (i1 == 21) {
//                    this.randomStyle = false;
//                    this.boldStyle = false;
//                    this.strikethroughStyle = false;
//                    this.underlineStyle = false;
//                    this.italicStyle = false;
//                    GlStateManager.color(this.red, this.blue, this.green, this.alpha);
//                }
//
//                ++i;
//            } else {
//                int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);
//
//                if (this.randomStyle && j != -1) {
//                    int k = this.getCharWidth(c0);
//                    char c1;
//
//                    while (true) {
//                        j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
//                        c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j);
//
//                        if (k == this.getCharWidth(c1)) {
//                            break;
//                        }
//                    }
//
//                    c0 = c1;
//                }
//
//                float f1 = this.unicodeFlag ? 0.5F : 1.0F;
//                boolean flag = (c0 == 0 || j == -1 || this.unicodeFlag) && shadow;
//
//                if (flag) {
//                    this.posX -= f1;
//                    this.posY -= f1;
//                }
//
//                float f = this.func_181559_a(c0, this.italicStyle);
//
//                if (flag) {
//                    this.posX += f1;
//                    this.posY += f1;
//                }
//
//                if (this.boldStyle) {
//                    this.posX += f1;
//
//                    if (flag) {
//                        this.posX -= f1;
//                        this.posY -= f1;
//                    }
//
//                    this.func_181559_a(c0, this.italicStyle);
//                    this.posX -= f1;
//
//                    if (flag) {
//                        this.posX += f1;
//                        this.posY += f1;
//                    }
//
//                    ++f;
//                }
//
//                if (this.strikethroughStyle) {
//                    Tessellator tessellator = Tessellator.getInstance();
//                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
//                    GlStateManager.disableTexture2D();
//                    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldrenderer.pos((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D).endVertex();
//                    worldrenderer.pos((double) (this.posX + f), (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D).endVertex();
//                    worldrenderer.pos((double) (this.posX + f), (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
//                    worldrenderer.pos((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
//                    tessellator.draw();
//                    GlStateManager.enableTexture2D();
//                }
//
//                if (this.underlineStyle) {
//                    Tessellator tessellator1 = Tessellator.getInstance();
//                    WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
//                    GlStateManager.disableTexture2D();
//                    worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
//                    int l = this.underlineStyle ? -1 : 0;
//                    worldrenderer1.pos((double) (this.posX + (float) l), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D).endVertex();
//                    worldrenderer1.pos((double) (this.posX + f), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D).endVertex();
//                    worldrenderer1.pos((double) (this.posX + f), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
//                    worldrenderer1.pos((double) (this.posX + (float) l), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
//                    tessellator1.draw();
//                    GlStateManager.enableTexture2D();
//                }
//
//                this.posX += (float) ((int) f);
//            }
//        }
//        if (Hyperium.INSTANCE.getHandlers().getConfigOptions().alternateFontRenderer && text.length() > 10) {
//            GL11.glEndList();
//            CachedString value = new CachedString(text, list, this.posX - posX, this.posY - posY);
//            if (shadow)
//                FontRendererData.INSTANCE.shadowStringCache.put(text, value);
//            else
//                FontRendererData.INSTANCE.normalStringCache.put(text, value);
//        }
//        GlStateManager.translate(-posX, -posY, 0F);
//    }
    @Shadow
    public abstract int getCharWidth(char character);

    /**
     * @author Sk1er
     */
    @Overwrite
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        } else {

            if (FontRendererData.INSTANCE.stringWidthCache.size() > Hyperium.INSTANCE.getHandlers().getConfigOptions().stringCacheSize)
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
