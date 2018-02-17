package com.hcc.mods.chromahud.displayitems.hcc.chromahud;

import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;


/**
 * Created by mitchellkatz on 6/25/17.
 */
public class CpsDisplay extends DisplayItem {

    public CpsDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }


    @Override
    public Dimension draw(int starX, double startY, boolean isConfig) {
        ElementRenderer.draw(starX, startY, "CPS: " + ElementRenderer.getCPS());
        if (isConfig)
            return new Dimension(Minecraft.getMinecraft().fontRendererObj.getStringWidth("CPS: " + ElementRenderer.getCPS()), 10);
        return new Dimension(0, 10);

    }


}
