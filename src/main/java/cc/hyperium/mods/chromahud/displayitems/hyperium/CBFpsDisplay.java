package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;

public class CBFpsDisplay extends DisplayItem {
    public CBFpsDisplay(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
        this.height = 10;
    }

    @Override
    public void draw(int x, double y, boolean config) {
        String string =  Minecraft.getDebugFPS()+" FPS";
        ElementRenderer.draw(x, y, string);
        this.height = Minecraft.getMinecraft().fontRendererObj.getStringWidth(string);
    }
}
