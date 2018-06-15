package cc.hyperium.mods.blockoverlay;

import cc.hyperium.event.DrawBlockHighlightEvent;
import cc.hyperium.event.InvokeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class BlockOverlayRender {
    private BlockOverlay mod;

    public BlockOverlayRender(BlockOverlay mod) {
        this.mod = mod;
    }

    @InvokeEvent
    public void onRenderBlockOverlay(DrawBlockHighlightEvent event) {
        if (this.mod.mc.thePlayer == null || this.mod.mc.theWorld == null || this.mod.getSettings().getOverlayMode() == BlockOverlayMode.DEFAULT) {
            return;
        }
        event.setCancelled(true);
        if (this.mod.getSettings().getOverlayMode() == BlockOverlayMode.NONE) {
            return;
        }

        this.drawOverlay(event.getPartialTicks());
    }

    private void drawOverlay(float partialTicks) {
        if (this.mod.mc.objectMouseOver == null || this.mod.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return;
        }
        MovingObjectPosition position = this.mod.mc.thePlayer.rayTrace(6.0, partialTicks);
        if (position == null || position.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return;
        }
        Block block = this.mod.mc.thePlayer.worldObj.getBlockState(position.getBlockPos()).getBlock();
        if (block == null || block == Blocks.air || block == Blocks.barrier || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.lava || block == Blocks.flowing_lava) {
            return;
        }
        float lineWidth = this.mod.getSettings().getLineWidth();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        if (lineWidth != 0.0f) {
            GL11.glLineWidth(this.mod.getSettings().getLineWidth());
        }
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        AxisAlignedBB box = block.getSelectedBoundingBox(this.mod.mc.theWorld, position.getBlockPos()).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-this.mod.mc.getRenderManager().viewerPosX, -this.mod.mc.getRenderManager().viewerPosY, -this.mod.mc.getRenderManager().viewerPosZ);

        if (this.mod.getSettings().getOverlayMode() == BlockOverlayMode.OUTLINE) {
            if (this.mod.getSettings().isChroma()) {
                float time = System.currentTimeMillis() % (10000L / this.mod.getSettings().getChromaSpeed()) / (10000.0f / this.mod.getSettings().getChromaSpeed());
                Color color = Color.getHSBColor(time, 1.0f, 1.0f);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, this.mod.getSettings().getOverlayAlpha());
            } else {
                GL11.glColor4f(this.mod.getSettings().getOverlayRed(), this.mod.getSettings().getOverlayGreen(), this.mod.getSettings().getOverlayBlue(), this.mod.getSettings().getOverlayAlpha());
            }
            if (lineWidth != 0.0f) {
                RenderGlobal.drawSelectionBoundingBox(box);
            }

        } else if (this.mod.getSettings().isChroma()) {
            float time = System.currentTimeMillis() % (10000L / this.mod.getSettings().getChromaSpeed()) / (10000.0f / this.mod.getSettings().getChromaSpeed());
            Color color = Color.getHSBColor(time, 1.0f, 1.0f);
            if (lineWidth != 0.0f) {
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
                RenderGlobal.drawSelectionBoundingBox(box);
            }
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, this.mod.getSettings().getOverlayAlpha());
            this.drawFilledBoundingBox(box);
        } else {
            if (lineWidth != 0.0f) {
                GL11.glColor4f(this.mod.getSettings().getOverlayRed(), this.mod.getSettings().getOverlayGreen(), this.mod.getSettings().getOverlayBlue(), 1.0f);
                RenderGlobal.drawSelectionBoundingBox(box);
            }
            GL11.glColor4f(this.mod.getSettings().getOverlayRed(), this.mod.getSettings().getOverlayGreen(), this.mod.getSettings().getOverlayBlue(), this.mod.getSettings().getOverlayAlpha());
            this.drawFilledBoundingBox(box);
        }
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void drawFilledBoundingBox(AxisAlignedBB box) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        tessellator.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        tessellator.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        tessellator.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        tessellator.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        tessellator.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        tessellator.draw();
    }
}
