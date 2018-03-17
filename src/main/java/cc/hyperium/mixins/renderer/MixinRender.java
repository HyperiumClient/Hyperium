package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
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
    protected RenderManager renderManager;

    @Shadow
    public abstract FontRenderer getFontRendererFromRenderManager();

    @Shadow
    protected abstract boolean canRenderName(T entity);

    /**
     * @author Sk1er
     */
    @Overwrite
    protected void renderOffsetLivingLabel(T entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
        this.renderLivingLabel(entityIn, str, x, y, z, Math.min(64 * 64, Hyperium.INSTANCE.getHandlers().getConfigOptions().renderNameDistance));
    }

    @Overwrite
    protected void renderName(T entity, double x, double y, double z) {
        if (this.canRenderName(entity)) {
            this.renderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, Math.min(64 * 64, Hyperium.INSTANCE.getHandlers().getConfigOptions().renderNameDistance));
        }
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
        double d0 = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);

        if (d0 <= (double) (maxDistance * maxDistance)) {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.0F, (float) y + entityIn.height + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 0;

            if (str.equals("deadmau5")) {
                i = -10;
            }

            int j = fontrenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            float a = Hyperium.INSTANCE.getHandlers().getConfigOptions().shadeNameTags ? .25F : 0F;
            worldrenderer.pos((double) (-j - 1), (double) (-1), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
            worldrenderer.pos((double) (-j - 1), (double) (8), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (8), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (-1), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();

            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

}

