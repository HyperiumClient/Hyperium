package com.hcc.mods.chromahud.displayitems.hcc.chromahud;


import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;


/**
 * Created by mitchellkatz on 6/21/17.
 */
public class PingDisplay extends DisplayItem {

    public PingDisplay(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
    }



    @Override
    public Dimension draw(int starX, double startY, boolean isConfig) {
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer != null) {
            String string = "Ping: " + Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID()).getResponseTime();
            ElementRenderer.draw(starX, startY, string);
            return new Dimension(Minecraft.getMinecraft().fontRendererObj.getStringWidth(string), 10);
        }
        return new Dimension(0, 0);
    }

}
