package cc.hyperium.mixins.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(World.class)
public interface IMixinWorld {

    @Invoker
    public void onEntityAdded(Entity entityIn);

    @Invoker
    boolean isChunkLoaded(int x, int z, boolean allowEmpty);
}
