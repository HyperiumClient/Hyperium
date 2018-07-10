package cc.hyperium.handlers.handlers.reach;

import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PlayerAttackEntityEvent;
import cc.hyperium.event.RenderEntitiesEvent;
import cc.hyperium.event.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ReachDisplay {


    public static double dis = 0.0;
    private List<Hit> hits = new ArrayList<>();
    private boolean locked = true;

    @InvokeEvent
    public void renderWorld(RenderEntitiesEvent event) {
        hits.removeIf(hit -> System.currentTimeMillis() - hit.start > 3000L);

        for (Hit hit : hits) {
            String string = Double.toString(hit.distance);
            RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
            FontRenderer fontrenderer = renderManager.getFontRenderer();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GlStateManager.pushMatrix();
            EntityPlayerSP entity = Minecraft.getMinecraft().thePlayer;
            float partialTicks = event.getPartialTicks();

            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;

            GlStateManager.translate(hit.pos.xCoord - d0, hit.pos.yCoord - d1, hit.pos.zCoord - d2);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

            int xMultiplier = 1; // Nametag x rotations should flip in front-facing 3rd person
            if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().gameSettings != null && Minecraft.getMinecraft().gameSettings.thirdPersonView == 2)
                xMultiplier = -1;
            GlStateManager.rotate(renderManager.playerViewX * xMultiplier, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 0;

            int j = fontrenderer.getStringWidth(string) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double) (-j - 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (-j - 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();

            fontrenderer.drawString(string, -j, 0, Color.RED.getRGB(), true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    @InvokeEvent
    public void tickEvent(TickEvent event) {
        locked = false;
    }

    @InvokeEvent
    public void attacc(PlayerAttackEntityEvent entityEvent) {
        if (!Settings.SHOW_HIT_DISTANCES)
            return;
        if (!(entityEvent.getEntity() instanceof EntityLivingBase))
            return;
        if (((EntityLivingBase) entityEvent.getEntity()).hurtTime > 0)
            return;
        if (locked)
            return;
        locked = true;
        EntityPlayerSP entity = Minecraft.getMinecraft().thePlayer;
        double d0 = 6;
        Vec3 vec3 = entity.getPositionEyes(0.0F);


        Vec3 vec31 = entity.getLook(0.0F);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
        float f = 1.0F;


        Entity entity1 = entityEvent.getEntity();
        float f1 = .1F;
        AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
        if (movingobjectposition == null) {
            System.out.println("intercept null");

            return;
        }
        Vec3 vec33 = movingobjectposition.hitVec;
        hits.add(new Hit(vec33, dis));

    }

    class Hit {

        private Vec3 pos;
        private double distance;
        private long start = System.currentTimeMillis();

        Hit(Vec3 pos, double distance) {
            this.pos = pos;
            this.distance = Math.round(distance * 100D) / 100D;
        }
    }
}
