package cc.hyperium.mixins.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityPlayer.class)
public interface IMixinEntityPlayer {
    @Invoker
    HoverEvent callGetHoverEvent();
}
