package cc.hyperium.handlers.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RenderPlayerAsBlock {
    public String[] blocks = new String[]{"stone", "sand", "dirt", "crafting_table_front","furnace_side","cobblestone","planks_oak","log_oak","red_sand"};

    public HashMap<UUID, String> cache = new HashMap<>();

    public void reDraw(AbstractClientPlayer entity, double x, double y, double z) {
        World entityWorld = entity.getEntityWorld();
        if (entityWorld != null) {
            if (entity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
                return;
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-entity.getRotationYawHead(), 0.0F, 1.0F, 0.0F);
            ResourceLocation res = new ResourceLocation("textures/blocks/" + cache.computeIfAbsent(entity.getUniqueID(), uuid -> blocks[ThreadLocalRandom.current().nextInt(blocks.length)]) + ".png");
            Minecraft.getMinecraft().getTextureManager().bindTexture(res);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
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
            wr.pos(-0.5, 0.0, -0.5).tex(1.0, 0.0).endVertex();
            wr.pos(0.5, 0.0, -0.5).tex(1.0, 1.0).endVertex();
            wr.pos(0.5, 0.0, 0.5).tex(0.0, 1.0).endVertex();
            wr.pos(-0.5, 0.0, 0.5).tex(0.0, 0.0).endVertex();
            Tessellator.getInstance().draw();
            GlStateManager.popMatrix();
        }
    }
}
