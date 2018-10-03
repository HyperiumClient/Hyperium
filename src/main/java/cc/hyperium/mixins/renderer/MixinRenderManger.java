package cc.hyperium.mixins.renderer;

import cc.hyperium.mixinsimp.renderer.IMixinRenderManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(RenderManager.class)
public class MixinRenderManger implements IMixinRenderManager {
    @Shadow
    private double renderPosX;

    @Shadow
    private double renderPosY;

    @Shadow
    private double renderPosZ;

    @Shadow
    private Map<String, RenderPlayer> skinMap;

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

    @Override
    public Map<String, RenderPlayer> getSkinMap() {
        return skinMap;
    }
}
