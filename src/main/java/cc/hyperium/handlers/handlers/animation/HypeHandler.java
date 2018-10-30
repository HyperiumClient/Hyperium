package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderEntitiesEvent;
import cc.hyperium.event.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HypeHandler {

    private List<Hype> hypes = new ArrayList<>();


    @InvokeEvent
    public void tickEvent(TickEvent event) {
        hypes.forEach(hype -> hype.age--);
        hypes.removeIf(hype -> hype.age < 0);
    }

    @InvokeEvent
    public void render(RenderEntitiesEvent event) {
        for (Hype hype : hypes) {
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

            EntityPlayer sender = hype.sender;
            if (sender == null) {
                continue;
            }
            double e0 = hype.sender.lastTickPosX + (hype.sender.posX - hype.sender.lastTickPosX) * (double) partialTicks;
            double e1 = hype.sender.lastTickPosY + (hype.sender.posY - hype.sender.lastTickPosY) * (double) partialTicks;
            double e2 = hype.sender.lastTickPosZ + (hype.sender.posZ - hype.sender.lastTickPosZ) * (double) partialTicks;


            GlStateManager.translate(e0 - d0, e1 - d1, e2 - d2);
            GlStateManager.translate(0, 3, 0);

            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

            int xMultiplier = 1; 
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

            String string = "Hype!";
            int j = fontrenderer.getStringWidth(string) / 2;
            GlStateManager.disableTexture2D();
            GlStateManager.scale(4, 4, 4);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double) (-j - 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (-j - 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();

            fontrenderer.drawString(string, -j, 0, Settings.REACH_COLOR_TYPE.equalsIgnoreCase("RGB") ? new Color(Settings.REACH_RED, Settings.REACH_GREEN, Settings.REACH_BLUE).getRGB() : Hyperium.INSTANCE.getModIntegration().getLevelhead().getRGBColor(), true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    public void hype(UUID uuid) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld == null)
            return;
        EntityPlayer player = theWorld.getPlayerEntityByUUID(uuid);
        if (player != null) {
            hypes.add(new Hype(player));
        }
    }

    class Hype {
        int age;
        EntityPlayer sender;

        public Hype(EntityPlayer sender) {
            this.sender = sender;
            age = 100;
        }
    }
}
