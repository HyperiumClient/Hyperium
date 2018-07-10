package cc.hyperium.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface IMixinMinecraft {

    @Accessor
    void setDisplayWidth(int width);

    @Accessor
    void setDisplayHeight(int height);


    @Accessor
    void setCurrentScreen(GuiScreen screen);
}
