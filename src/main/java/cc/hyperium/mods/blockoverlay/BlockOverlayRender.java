/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.blockoverlay;

import cc.hyperium.event.render.DrawBlockHighlightEvent;
import cc.hyperium.event.InvokeEvent;

import java.awt.Color;

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

public class BlockOverlayRender {
    private BlockOverlay mod;

    public BlockOverlayRender(BlockOverlay mod) {
        this.mod = mod;
    }

    @InvokeEvent
    public void onRenderBlockOverlay(DrawBlockHighlightEvent event) {
        if (BlockOverlay.mc.thePlayer == null || BlockOverlay.mc.theWorld == null || mod.getSettings().getOverlayMode() == BlockOverlayMode.DEFAULT) {
            return;
        }
        event.setCancelled(true);
        if (mod.getSettings().getOverlayMode() == BlockOverlayMode.NONE) return;

        drawOverlay(event.getPartialTicks());
        GL11.glLineWidth(1.0F);
    }

    private void drawOverlay(float partialTicks) {
        if (BlockOverlay.mc.objectMouseOver == null || BlockOverlay.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return;
        }

        MovingObjectPosition position = BlockOverlay.mc.thePlayer.rayTrace(6.0, partialTicks);
        if (position == null || position.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return;
        }

        Block block = BlockOverlay.mc.thePlayer.worldObj.getBlockState(position.getBlockPos()).getBlock();
        if (block == null || block == Blocks.air || block == Blocks.barrier || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.lava || block == Blocks.flowing_lava) {
            return;
        }

        float lineWidth = mod.getSettings().getLineWidth();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        if (lineWidth != 0.0f) GL11.glLineWidth(mod.getSettings().getLineWidth());

        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        AxisAlignedBB box = block.getSelectedBoundingBox(BlockOverlay.mc.theWorld, position.getBlockPos()).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-BlockOverlay.mc.getRenderManager().viewerPosX, -BlockOverlay.mc.getRenderManager().viewerPosY, -BlockOverlay.mc.getRenderManager().viewerPosZ);

        if (mod.getSettings().getOverlayMode() == BlockOverlayMode.OUTLINE) {
            if (mod.getSettings().isChroma()) {
                float time = System.currentTimeMillis() % (10000L / mod.getSettings().getChromaSpeed()) / (10000.0f / mod.getSettings().getChromaSpeed());
                Color color = Color.getHSBColor(time, 1.0f, 1.0f);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, mod.getSettings().getOverlayAlpha());
            } else {
                GL11.glColor4f(mod.getSettings().getOverlayRed(), mod.getSettings().getOverlayGreen(), mod.getSettings().getOverlayBlue(), mod.getSettings().getOverlayAlpha());
            }

            if (lineWidth != 0.0f) RenderGlobal.drawSelectionBoundingBox(box);

        } else if (mod.getSettings().isChroma()) {
            float time = System.currentTimeMillis() % (10000L / mod.getSettings().getChromaSpeed()) / (10000.0f / mod.getSettings().getChromaSpeed());
            Color color = Color.getHSBColor(time, 1.0f, 1.0f);

            if (lineWidth != 0.0f) {
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
                RenderGlobal.drawSelectionBoundingBox(box);
            }

            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, mod.getSettings().getOverlayAlpha());
            drawFilledBoundingBox(box);
        } else {
            if (lineWidth != 0.0f) {
                GL11.glColor4f(mod.getSettings().getOverlayRed(), mod.getSettings().getOverlayGreen(), mod.getSettings().getOverlayBlue(), 1.0f);
                RenderGlobal.drawSelectionBoundingBox(box);
            }

            GL11.glColor4f(mod.getSettings().getOverlayRed(), mod.getSettings().getOverlayGreen(), mod.getSettings().getOverlayBlue(), mod.getSettings().getOverlayAlpha());
            drawFilledBoundingBox(box);
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
