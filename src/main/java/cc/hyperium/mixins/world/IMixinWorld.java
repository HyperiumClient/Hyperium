package cc.hyperium.mixins.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(World.class)
public interface IMixinWorld {
    @Accessor
    float getRainingStrength();

    @Invoker
    boolean callIsChunkLoaded(int x, int z, boolean allowEmpty);

    @Invoker
    void callOnEntityRemoved(Entity entityIn);

    @Accessor
    void setProcessingLoadedTiles(boolean in);
}
