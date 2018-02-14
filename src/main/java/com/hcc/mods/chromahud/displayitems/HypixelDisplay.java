package com.hcc.mods.chromahud.displayitems;

import com.hcc.HCC;
import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;

public class HypixelDisplay extends DisplayItem {
    public HypixelDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }

    @Override
    public Dimension draw(int x, double y, boolean config) {
        String string = "Hypixel: " + HCC.INSTANCE.getHandlers().getHypixelDetector().isHypixel();
        ElementRenderer.draw(x, y, string);
        //TODO remove specific reference
        return new Dimension(config ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) : 0, 10);
    }
}
