package cc.hyperium.handlers.handlers;

import cc.hyperium.mixinsimp.renderer.IMixinRenderManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class RenderPlayerAsBlock {

    public static void reDraw(AbstractClientPlayer entity, double x, double y, double z) {
        World entityWorld = entity.getEntityWorld();
        if (entityWorld != null) {
            IMixinRenderManager renderManager = (IMixinRenderManager) Minecraft.getMinecraft().getRenderManager();
            int x1 = (int) (x + renderManager.getPosX());
            int y1 = (int) (y + renderManager.getPosY() - 1);
            int z1 = (int) (z + renderManager.getPosZ());
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            BlockPos pos = new BlockPos(x1, y1, z1);
            IBlockState blockState = entityWorld.getBlockState(pos);
            BlockPos pos1 = new BlockPos(x1, y1 + 1, z1);

            WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
            List<VertexFormatElement> elements = worldRenderer.getVertexFormat().getElements();
//            elements.add(DefaultVertexFormats.TEX_2S);
            blockrendererdispatcher.renderBlock(blockState, pos1, new RegionRenderCache(entityWorld, pos1.add(-1, -1, -1), pos1.add(1, 1, 1), 1), worldRenderer);
//            elements.remove(DefaultVertexFormats.TEX_2S);


        }
    }
}
