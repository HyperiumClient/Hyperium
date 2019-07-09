package cc.hyperium.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiButton.class)
public interface MixinGuiButton2 {
    @Accessor
    ResourceLocation getButtonTextures();

    @Accessor
    int getHeight();

    @Accessor
    void setHovered(boolean hovered);

    @Invoker
    void callMouseDragged(Minecraft mc, int mouseX, int mouseY);

    @Accessor
    boolean isHovered();
}
