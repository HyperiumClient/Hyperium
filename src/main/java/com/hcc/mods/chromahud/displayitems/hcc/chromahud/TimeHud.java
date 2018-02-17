package com.hcc.mods.chromahud.displayitems.hcc.chromahud;

import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mitchellkatz on 6/26/17.
 */
public class TimeHud extends DisplayItem {
    private String format;

    public TimeHud(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.format = data.optString("format");
        if (this.format.isEmpty()) {
            this.format = "HH:mm:ss";
        }

    }


    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        data.put("format", format);
        this.format = format;
    }

    @Override
    public Dimension draw(int starX, double startY, boolean isConfig) {
        try {
            String string = new SimpleDateFormat(format).format(new Date(System.currentTimeMillis()));
            ElementRenderer.draw(starX, startY, string);
            return new Dimension(isConfig ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) : 0, 10);
        } catch (Exception e) {
            ElementRenderer.draw(starX, startY, "Invalid");
        }
        return new Dimension(0, 0);
    }

}
