package cc.hyperium.mixins.renderer;

import cc.hyperium.mixinsimp.renderer.HyperiumRender;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Sk1er
 */
@Mixin(Render.class)
public abstract class MixinRender<T extends Entity> {

    @Shadow
    @Final
    private RenderManager renderManager;
    private HyperiumRender<T> hyperiumRender = new HyperiumRender<>((Render<T>) (Object) this);

    @Shadow
    public abstract FontRenderer getFontRendererFromRenderManager();

    @Shadow
    protected abstract boolean canRenderName(T entity);

    /**
     * @author Sk1er
     */
    @Overwrite
    protected void renderOffsetLivingLabel(T entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
        hyperiumRender.renderOffsetLivingLabel(entityIn, x, y, z, str, p_177069_9_, p_177069_10_);
    }

    @Overwrite
    protected void renderName(T entity, double x, double y, double z) {
        hyperiumRender.renderName(entity, x, y, z);
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
        hyperiumRender.renderLivingLabel(entityIn, str, x, y, z, maxDistance, renderManager);
    }


}

