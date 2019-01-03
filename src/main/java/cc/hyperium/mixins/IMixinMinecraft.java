package cc.hyperium.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface IMixinMinecraft {

    @Accessor
    void setDisplayWidth(int width);

    @Accessor
    void setDisplayHeight(int height);

    @Accessor
    void setCurrentScreen(GuiScreen screen);

    @Invoker
    void callResize(int width, int height);

    @Accessor
    Timer getTimer();
}
