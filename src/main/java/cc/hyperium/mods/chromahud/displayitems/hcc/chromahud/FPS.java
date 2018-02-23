package cc.hyperium.mods.chromahud.displayitems.Hyperium.chromahud;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;




/**
 * Created by mitchellkatz on 5/30/17.
 */
public class FPS extends DisplayItem {

    public FPS(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
    }


    @Override
    public Dimension draw(int starX, double startY, boolean ignored) {
        String string = "FPS: " + Minecraft.getDebugFPS();
        ElementRenderer.draw(starX, startY, string);
        return new Dimension(Minecraft.getMinecraft().fontRendererObj.getStringWidth(string), 10);
    }


}
