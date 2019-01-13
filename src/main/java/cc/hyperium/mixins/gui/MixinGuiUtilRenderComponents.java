package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumGuiUtilRenderComponents;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GuiUtilRenderComponents.class)
abstract class MixinGuiUtilRenderComponents {

    /**
     * @author Sk1er
     * @reason Fixed next line resetting chat formatting
     */
    @Overwrite
    public static List<IChatComponent> func_178908_a(IChatComponent p_178908_0_, int p_178908_1_, FontRenderer p_178908_2_, boolean p_178908_3_, boolean p_178908_4_) {
        return HyperiumGuiUtilRenderComponents.func_178908_a(p_178908_0_, p_178908_1_, p_178908_2_, p_178908_3_, p_178908_4_);
    }
}
