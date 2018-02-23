package cc.hyperium.mods.chromahud.displayitems.Hyperium.chromahud;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell Katz on 6/1/2017.
 */
public class DirectionHUD extends DisplayItem {
    private static final String[] dir = {"South", "South West", "West", "North West", "North", "North East", "East", "South East"};


    public DirectionHUD(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
    }


    public Dimension draw(int x, double y, boolean isConfig) {
        List<String> list = new ArrayList<>();
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            int d = (int) player.rotationYaw;
            if (d < 0)
                d += 360;
            d += 22;
            int direction = (d % 360);
            direction = direction / (45);
            try {
                while (direction < 0)
                    direction += 8;

                list.add(dir[direction]);
            } catch (Exception e) {

            }
        }
        ElementRenderer.draw(x, y, list);
        return new Dimension(isConfig ? ElementRenderer.maxWidth(list) : 0, 10);
    }

}
