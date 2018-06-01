package cc.hyperium.handlers.handlers;

import cc.hyperium.mixinsimp.renderer.IMixinRenderManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderPlayerAsBlock {

    public static void reDraw(AbstractClientPlayer entity, double x, double y, double z) {
        World entityWorld = entity.getEntityWorld();
        if (entityWorld != null) {
            IMixinRenderManager renderManager = (IMixinRenderManager) Minecraft.getMinecraft().getRenderManager();
            int x1 = (int) (x + renderManager.getPosX());
            int y1 = (int) (y + renderManager.getPosY()-1);
            int z1 = (int) (z + renderManager.getPosZ());
            System.out.println("x1 = " + x1);
            System.out.println("y1 = " + y1);
            System.out.println("z1 = " + z1);
            IBlockState blockState = entityWorld.getBlockState(new BlockPos(x1, y1, z1));
            String res1 = "air";
            if (blockState != null) {
                Block block = blockState.getBlock();
                System.out.println(block.getMaterial());
                res1 = block.getUnlocalizedName().replaceFirst("tile.", "");
            }
            System.out.println(res1);
            final ResourceLocation res = new ResourceLocation("textures/blocks/" + res1 + ".png");
            Minecraft.getMinecraft().getTextureManager().bindTexture(res);
            GlStateManager.color(0.5f, 0.5f, 0.5f);
            GlStateManager.disableLighting();
            final WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
            wr.begin(7, DefaultVertexFormats.POSITION_TEX);
            wr.pos(-0.5, 1.0, 0.5).tex(1.0, 0.0).endVertex();
            wr.pos(-0.5, 0.0, 0.5).tex(1.0, 1.0).endVertex();
            wr.pos(0.5, 0.0, 0.5).tex(0.0, 1.0).endVertex();
            wr.pos(0.5, 1.0, 0.5).tex(0.0, 0.0).endVertex();
            wr.pos(0.5, 1.0, 0.5).tex(1.0, 0.0).endVertex();
            wr.pos(0.5, 0.0, 0.5).tex(1.0, 1.0).endVertex();
            wr.pos(0.5, 0.0, -0.5).tex(0.0, 1.0).endVertex();
            wr.pos(0.5, 1.0, -0.5).tex(0.0, 0.0).endVertex();
            wr.pos(0.5, 1.0, -0.5).tex(1.0, 0.0).endVertex();
            wr.pos(0.5, 0.0, -0.5).tex(1.0, 1.0).endVertex();
            wr.pos(-0.5, 0.0, -0.5).tex(0.0, 1.0).endVertex();
            wr.pos(-0.5, 1.0, -0.5).tex(0.0, 0.0).endVertex();
            wr.pos(-0.5, 1.0, -0.5).tex(1.0, 0.0).endVertex();
            wr.pos(-0.5, 0.0, -0.5).tex(1.0, 1.0).endVertex();
            wr.pos(-0.5, 0.0, 0.5).tex(0.0, 1.0).endVertex();
            wr.pos(-0.5, 1.0, 0.5).tex(0.0, 0.0).endVertex();
            wr.pos(-0.5, 1.0, -0.5).tex(1.0, 0.0).endVertex();
            wr.pos(-0.5, 1.0, 0.5).tex(1.0, 1.0).endVertex();
            wr.pos(0.5, 1.0, 0.5).tex(0.0, 1.0).endVertex();
            wr.pos(0.5, 1.0, -0.5).tex(0.0, 0.0).endVertex();
            wr.pos(-0.5, 1.0, -0.5).tex(1.0, 0.0).endVertex();
            wr.pos(0.5, 0.0, -0.5).tex(1.0, 1.0).endVertex();
            wr.pos(0.5, 0.0, 0.5).tex(0.0, 1.0).endVertex();
            wr.pos(-0.5, 0.0, 0.5).tex(0.0, 0.0).endVertex();
            Tessellator.getInstance().draw();

        }
    }
}
