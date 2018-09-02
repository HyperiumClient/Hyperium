package cc.hyperium.mods.chromahud.displayitems.hyperium;

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
        this.height = 75;
    }


    @Override
    public void draw(int x, double y, boolean config) {
        GlStateManager.color(1, 1, 1);
//
        GlStateManager.translate(x, y, 5);
        final int mouseX = 0;
        final int mouseY = 0;
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();

        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.depthFunc(519);
        GlStateManager.disableCull();

//        GlStateManager.rotate(150,0,1.0F,0);
        GuiInventory.drawEntityOnScreen(0, 0, 50, 0, 0, Minecraft.getMinecraft().thePlayer);
        GlStateManager.depthFunc(515);
GlStateManager.resetColor();
GlStateManager.color(1.0F,1.0F,1.0F,1.0F);
    }
}
