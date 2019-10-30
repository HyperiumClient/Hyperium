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

package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderEntitiesEvent;
import cc.hyperium.event.client.TickEvent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class YeetHandler {

    private List<Yeet> yeets = new ArrayList<>();


    @InvokeEvent
    public void tickEvent(TickEvent event) {
        yeets.removeIf(yeet -> {
            yeet.age--;
            return yeet.age < 0;
        });
    }

    @InvokeEvent
    public void render(RenderEntitiesEvent event) {
        for (Yeet yeet : yeets) {
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

            EntityPlayer sender = yeet.sender;
            if (sender == null) continue;
            double e0 = yeet.sender.lastTickPosX + (yeet.sender.posX - yeet.sender.lastTickPosX) * (double) partialTicks;
            double e1 = yeet.sender.lastTickPosY + (yeet.sender.posY - yeet.sender.lastTickPosY) * (double) partialTicks;
            double e2 = yeet.sender.lastTickPosZ + (yeet.sender.posZ - yeet.sender.lastTickPosZ) * (double) partialTicks;


            GlStateManager.translate(e0 - d0, e1 - d1, e2 - d2);
            GlStateManager.translate(0, 3, 0);

            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

            int xMultiplier = 1; // Nametag x rotations should flip in front-facing 3rd person

            if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().gameSettings != null && Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
                xMultiplier = -1;
            }

            GlStateManager.rotate(renderManager.playerViewX * xMultiplier, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 0;

            String string = "YEET";
            int j = fontrenderer.getStringWidth(string) / 2;
            GlStateManager.disableTexture2D();
            GlStateManager.scale(4, 4, 4);
            worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();

            fontrenderer.drawString(string, -j, 0, 16777215, true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    public void yeet(UUID uuid) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld == null) return;
        EntityPlayer player = theWorld.getPlayerEntityByUUID(uuid);
        if (player != null) yeets.add(new Yeet(player));
    }

    static class Yeet {
        int age;
        EntityPlayer sender;

        Yeet(EntityPlayer sender) {
            this.sender = sender;
            age = 100;
        }
    }
}
