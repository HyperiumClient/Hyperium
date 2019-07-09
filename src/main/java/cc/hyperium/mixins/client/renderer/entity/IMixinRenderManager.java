package cc.hyperium.mixins.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RenderManager.class)
public interface IMixinRenderManager {

    @Accessor Map<String, RenderPlayer> getSkinMap();

}
