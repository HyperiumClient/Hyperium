package cc.hyperium.mods.blockoverlay;

import cc.hyperium.event.DrawBlockHighlightEvent;
import cc.hyperium.event.InvokeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.WorldSettings;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class BlockOverlayRender {
    @InvokeEvent
    public void onDrawBlockHighlight(final DrawBlockHighlightEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null || (!Minecraft.getMinecraft().playerController.getCurrentGameType().equals(WorldSettings.GameType.SURVIVAL) && !Minecraft.getMinecraft().playerController.getCurrentGameType().equals(WorldSettings.GameType.CREATIVE))) {
            return;
        }
        if (BlockOverlay.mode.equals(BlockOverlayMode.DEFAULT)) {
            return;
        }
        event.setCancelled(true);
        if (BlockOverlay.mode.equals(BlockOverlayMode.NONE)) {
            return;
        }
        this.drawOverlay();
    }

    public void drawOverlay() {
        if (Minecraft.getMinecraft().objectMouseOver == null || !Minecraft.getMinecraft().objectMouseOver.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK)) {
            return;
        }
        final MovingObjectPosition position = Minecraft.getMinecraft().thePlayer.rayTrace(6.0, 0.0f);
        if (position == null || !position.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK)) {
            return;
        }
        final Block block = Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(position.getBlockPos()).getBlock();
        if (block == null || block.equals(Blocks.air) || block.equals(Blocks.barrier) || block.equals(Blocks.water) || block.equals(Blocks.flowing_water) || block.equals(Blocks.lava) || block.equals(Blocks.flowing_lava)) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(BlockOverlay.lineWidth);
        final AxisAlignedBB box = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, position.getBlockPos()).offset(-Minecraft.getMinecraft().getRenderManager().viewerPosX, -Minecraft.getMinecraft().getRenderManager().viewerPosY, -Minecraft.getMinecraft().getRenderManager().viewerPosZ).expand(0.0010000000474974513, 0.0010000000474974513, 0.0010000000474974513);
        if (BlockOverlay.mode.equals(BlockOverlayMode.OUTLINE)) {
            if (BlockOverlay.isChroma) {
                final double millis = (double) (System.currentTimeMillis() % (10000L / BlockOverlay.chromaSpeed) / (10000.0f / BlockOverlay.chromaSpeed));
                final Color color = Color.getHSBColor((float) millis, 0.8f, 0.8f);
                GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, BlockOverlay.alpha);
            } else {
                GL11.glColor4f(BlockOverlay.red, BlockOverlay.green, BlockOverlay.blue, BlockOverlay.alpha);
            }
            RenderGlobal.drawSelectionBoundingBox(box);
        } else if (BlockOverlay.isChroma) {
            final double millis = (double) (System.currentTimeMillis() % (10000L / BlockOverlay.chromaSpeed) / (10000.0f / BlockOverlay.chromaSpeed));
            final Color color = Color.getHSBColor((float) millis, 0.8f, 0.8f);
            GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, 1.0f);
            RenderGlobal.drawSelectionBoundingBox(box);
            GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, BlockOverlay.alpha);
            this.drawFilledBoundingBox(box);
        } else {
            GL11.glColor4f(BlockOverlay.red, BlockOverlay.green, BlockOverlay.blue, 1.0f);
            RenderGlobal.drawSelectionBoundingBox(box);
            GL11.glColor4f(BlockOverlay.red, BlockOverlay.green, BlockOverlay.blue, BlockOverlay.alpha);
            this.drawFilledBoundingBox(box);
        }
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    public void drawFilledBoundingBox(final AxisAlignedBB box) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
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
