package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.Hyperium;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCape;

public class HyperiumLayerCape {

    private LayerCape parent;

    public HyperiumLayerCape(LayerCape parent) {
        this.parent = parent;
    }

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn) {
        if (entitylivingbaseIn.isSneaking() && !Hyperium.INSTANCE.isOptifineInstalled()) {
            // Optifine does this by default.
            GlStateManager.translate(0, 0, .1);
        }
    }
}
