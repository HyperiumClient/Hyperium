package com.hcc.mods.chromahud.displayitems.hcc.chromahud;

import com.google.gson.JsonObject;
import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class CordsDisplay extends DisplayItem {

    public int state = 0;
    public int precision = 1;
    private JsonObject raw;


    public CordsDisplay(JsonHolder options, int orderinal) {
        super(options, orderinal);
        state = options.optInt("state");
        this.precision = options.optInt("precision");

    }

    @Override
    public void save() {
        data.put("state", state);
        data.put("precision", precision);
    }

    @Override
    public String toString() {
        return "CordsDisplay{" +
                "state=" + state +
                '}';
    }

    @Override
    public Dimension draw(int x, double y, boolean isConfig) {
        List<String> tmp = new ArrayList<>();
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            StringBuilder start = new StringBuilder("0");
            if (precision > 0)
                start.append(".");
            for (int i = 0; i < precision; i++) {
                start.append("0");
            }
            DecimalFormat df = new DecimalFormat(start.toString());

            if (state == 0) {
                tmp.add("X: " + df.format(player.posX) +
                        " Y: " + df.format(player.posY) +
                        " Z: " + df.format(player.posZ));
            } else if (state == 1) {
                tmp.add("X: " + df.format(player.posX));
                tmp.add("Y: " + df.format(player.posY));
                tmp.add("Z: " + df.format(player.posZ));
            } else tmp.add("Illegal state of cords unit (" + state + ")");
        } else tmp.add("X: null, Y: null, Z: null");
        ElementRenderer.draw(x, y, tmp);
        return new Dimension(isConfig ? ElementRenderer.maxWidth(tmp) : 0, tmp.size() * 10);

    }
}
