package cc.hyperium.mixins.renderer;

import cc.hyperium.mixinsimp.renderer.IMixinRenderManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderManager.class)
public class MixinRenderManger implements IMixinRenderManager {
    @Shadow
    private double renderPosX;

    @Shadow
    private double renderPosY;

    @Shadow private double renderPosZ;

    @Override
    public double getPosX() {
        return renderPosX;
    }

    @Override
    public double getPosY() {
        return renderPosY;
    }

    @Override
    public double getPosZ() {
        return renderPosZ;
    }
}
