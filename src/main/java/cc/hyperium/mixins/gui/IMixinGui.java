package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface IMixinGui {
    @Accessor
    void setZLevel(float zLevel);
}
