package com.chattriggers.ctjs.minecraft.mixins;

import net.minecraft.client.gui.ChatLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC<=10809
import net.minecraft.util.IChatComponent;
//#else
//$$ import net.minecraft.util.text.ITextComponent;
//#endif

@Mixin(ChatLine.class)
public interface MixinChatLine {
    @Accessor
    //#if MC<=10809
    void setLineString(IChatComponent chatComponent);
    //#else
    //$$ void setLineString(ITextComponent chatComponent);
    //#endif

    @Accessor
    void setChatLineID(int id);
}
