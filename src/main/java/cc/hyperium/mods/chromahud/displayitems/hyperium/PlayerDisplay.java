package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;

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
        
        GlStateManager.translate(x, y, 0);
        
        // This renders really weirdly, I suspect its because of the mixin overrides on the living entity renderer
        GuiInventory.drawEntityOnScreen(0, 0, 30, 0, 0, Minecraft.getMinecraft().thePlayer);
    }
}
