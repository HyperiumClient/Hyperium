package cc.hyperium.cosmetics.companions.hamster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderHamster extends RenderLiving<EntityHamster> {
    private ResourceLocation hamsterTexture = new ResourceLocation("textures/cosmetics/companions/hamsterbrown.png");

    public RenderHamster(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new HamsterModel(), 0.2f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHamster entity) {
        return hamsterTexture;
    }

    @Override
    protected void preRenderCallback(EntityHamster entitylivingbaseIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);

        GlStateManager.scale(0.5, 0.5, 0.5);
    }
}
