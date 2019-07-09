package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

/*
 * Created by Cubxity on 22/04/2018
 */
public class PlayerDisplay extends DisplayItem {

    public PlayerDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.width = 51;
        this.height = 100;
    }


    @Override
    public void draw(int x, double y, boolean config) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1);

        GlStateManager.translate(x, y, 5);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();

        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();

        GlStateManager.rotate(30, 0, 1.0F, 0);
        GuiInventory.drawEntityOnScreen(0, 100, 50, 0, 0, Minecraft.getMinecraft().thePlayer);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }
}
