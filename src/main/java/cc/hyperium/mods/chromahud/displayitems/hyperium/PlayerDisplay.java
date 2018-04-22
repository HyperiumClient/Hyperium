package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/*
 * Created by Cubxity on 22/04/2018
 */
public class PlayerDisplay extends DisplayItem {
    public PlayerDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.width = 51;
        this.height = 75;
    }

    @Override
    public void draw(int x, double y, boolean config) {
//        GuiInventory.drawEntityOnScreen(20, 250, 30, 0, 0, Minecraft.getMinecraft() .thePlayer);
        float scale = 30f;
        int posY = (int) (y + 60);
        int posX = (x + 20);
        float mouseX = (float) (x + 51) - Mouse.getX();
        float mouseY = (float) (y + 75 - 50) - Mouse.getY();
        EntityLivingBase ent = Minecraft.getMinecraft().thePlayer;

        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.scale(scale, scale, 0);
        GlStateManager.enableColorMaterial();
        RenderHelper.enableStandardItemLighting();

        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;

        GlStateManager.translate((float) posX / scale, (float) posY / scale, 50.0F);
        GlStateManager.rotate(90f, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) Math.atan((double) (mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);

        ent.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;

        GlStateManager.translate(0.0F, 0.0F, 0.0F);

        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);

        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
    }
}
