package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiContainer.class)
public interface IMixinGuiContainer {
    @Accessor
    void setGuiLeft(int guiLeft);
}
