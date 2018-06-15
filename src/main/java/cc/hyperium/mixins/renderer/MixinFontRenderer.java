package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.FontRendererData;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {


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
