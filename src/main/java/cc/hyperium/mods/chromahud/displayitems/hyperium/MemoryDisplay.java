package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class MemoryDisplay extends DisplayItem {

    public MemoryDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }

    @Override
    public void draw(int x, double y, boolean config) {
        int mbDiv = 1048576;
        long maxMemory = Runtime.getRuntime().maxMemory() / mbDiv;
        long totalMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / mbDiv;

        String displayString = totalMemory + " / " + (maxMemory == Long.MAX_VALUE ? "No limit" : maxMemory) + "MB";
        ElementRenderer.draw(x, y, displayString);
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

        this.height = fr.FONT_HEIGHT * ElementRenderer.getCurrentScale();
        this.width = fr.getStringWidth(displayString) * ElementRenderer.getCurrentScale();
    }
}
