package cc.hyperium.mixinsimp.entity;

import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.entity.projectile.EntityFishHook;
import org.lwjgl.opengl.GL11;

public class HyperiumRenderFish {
    private RenderFish parent;


    public HyperiumRenderFish(RenderFish parent) {
        this.parent = parent;
    }

    public void doRender(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
        // Set line width to normal to prevent becoming thick.
        GL11.glLineWidth(1.0F);
    }
}
