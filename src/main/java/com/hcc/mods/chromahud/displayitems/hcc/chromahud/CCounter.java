package com.hcc.mods.chromahud.displayitems.hcc.chromahud;


import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;

/**
 * Created by mitchellkatz on 5/30/17.
 */
public class CCounter extends DisplayItem {

    public CCounter(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
    }


    @Override
    public Dimension draw(int starX, double startY, boolean ignored) {
        String string = ElementRenderer.getCValue();
        ElementRenderer.draw(starX, startY, string);
        return new Dimension(Minecraft.getMinecraft().fontRendererObj.getStringWidth(string), 10);
    }


}
