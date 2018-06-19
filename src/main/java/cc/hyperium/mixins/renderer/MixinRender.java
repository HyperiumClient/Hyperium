package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.purchases.packages.EarsCosmetic;
import cc.hyperium.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
    private RenderManager renderManager;

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
            float offset = Utils.INSTANCE.calculateDeadmauEarsOffset(entityIn);
            GlStateManager.translate((float) x + 0.0F, (float) y + offset + entityIn.height + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

            int xMultiplier = 1; // Nametag x rotations should flip in front-facing 3rd person
            if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().gameSettings != null && Minecraft.getMinecraft().gameSettings.thirdPersonView == 2)
                xMultiplier = -1;
            GlStateManager.rotate(this.renderManager.playerViewX * xMultiplier, 1.0F, 0.0F, 0.0F);
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
            float a = .25F;
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

