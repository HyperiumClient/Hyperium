package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiNewChat.class)
public interface IMixinGuiNewChat {
    @Accessor
    void setIsScrolled(boolean isScrolled);
}
