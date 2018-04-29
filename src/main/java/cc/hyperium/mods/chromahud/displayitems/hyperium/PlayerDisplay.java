package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/*
 * Created by Cubxity on 22/04/2018
 */
public class PlayerDisplay extends DisplayItem {
    public PlayerDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.width = 51;
        this.height = 65;
    }

    @Override
    public void draw(int x, double y, boolean config) {
        GlStateManager.pushMatrix();
//        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.color(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GuiInventory.drawEntityOnScreen((int) (x - width / 2), (int) (y + height), 30, 0, 0, Minecraft.getMinecraft().thePlayer);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.popMatrix();
    }
}
