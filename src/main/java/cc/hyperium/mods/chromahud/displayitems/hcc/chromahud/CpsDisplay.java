package cc.hyperium.mods.chromahud.displayitems.Hyperium.chromahud;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
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
