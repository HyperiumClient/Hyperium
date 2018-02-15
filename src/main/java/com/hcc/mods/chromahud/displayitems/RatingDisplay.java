package com.hcc.mods.chromahud.displayitems;

import com.hcc.HCC;
import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;

import java.text.NumberFormat;
import java.util.Locale;

public class RatingDisplay extends DisplayItem {
    private static final NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
    public RatingDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }

    @Override
    public Dimension draw(int x, double y, boolean config) {
        String string = "Rating: " + format.format(HCC.INSTANCE.getHandlers().getValueHandler().getRankedRating());
        if (data.optBoolean("delta")) {
            string += " (" + HCC.INSTANCE.getHandlers().getValueHandler().getDeltaRankedRating() + ")";
        }

        ElementRenderer.draw(x, y, string);
        return new Dimension(config ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) : 0, 10);
    }
}
