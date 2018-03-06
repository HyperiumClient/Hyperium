package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;

public class CBCpsDisplay extends DisplayItem {
    public CBCpsDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.height = 10;
    }
    @Override
    public void draw(int starX, double startY, boolean isConfig) {
        ElementRenderer.draw(starX, startY, ElementRenderer.getCPS()+" CPS");
        if (isConfig)
            this.width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(ElementRenderer.getCPS() +" CPS");

    }
}
