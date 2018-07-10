package cc.hyperium.mixins.entity;

import net.minecraft.entity.Entity;
import net.minecraft.event.HoverEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface IMixinEntity {
    @Invoker
    HoverEvent callGetHoverEvent();
}
