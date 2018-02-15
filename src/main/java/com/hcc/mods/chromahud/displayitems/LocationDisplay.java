package com.hcc.mods.chromahud.displayitems;

import com.hcc.HCC;
import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;


/**
 * Created by mitchellkatz on 5/30/17.
 */
public class LocationDisplay extends DisplayItem {

    public LocationDisplay(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
    }


    @Override
    public Dimension draw(int starX, double startY, boolean config) {
        String string = "Location: " + HCC.INSTANCE.getHandlers().getLocationHandler().getLocation();
        ElementRenderer.draw(starX, startY, string);
        return new Dimension(config ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) : 0, 10);
    }


}
