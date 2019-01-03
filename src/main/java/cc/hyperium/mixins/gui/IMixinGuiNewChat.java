package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(GuiNewChat.class)
public interface IMixinGuiNewChat {
    @Accessor
    void setIsScrolled(boolean isScrolled);

    @Accessor
    List<ChatLine> getChatLines();

    @Accessor(value = "field_146253_i")
    List<ChatLine> getDrawnChatLines();
}
