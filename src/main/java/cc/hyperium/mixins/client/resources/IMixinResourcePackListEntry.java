package cc.hyperium.mixins.client.resources;

import net.minecraft.client.resources.ResourcePackListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ResourcePackListEntry.class)
public interface IMixinResourcePackListEntry {
    @Invoker String callFunc_148312_b(); // getResourcePackName
}
