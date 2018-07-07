package cc.hyperium.mixins.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(World.class)
public interface IMixinWorld {

    @Invoker
    void callOnEntityAdded(Entity entityIn);

    @Invoker
    boolean callIsChunkLoaded(int x, int z, boolean allowEmpty);
}
