package cc.hyperium.mixins.gui;

import net.minecraft.client.resources.ResourcePackListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ResourcePackListEntry.class)
public interface IMixinGuiResourcePack {
    @Invoker
    int callFunc_183019_a();

    @Invoker
    String callFunc_148312_b();

    @Invoker
    String callFunc_148311_a();
}
