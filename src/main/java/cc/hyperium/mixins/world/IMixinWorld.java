package cc.hyperium.mixins.world;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(World.class)
public interface IMixinWorld {
    @Accessor
    float getRainingStrength();
}
