package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.statistics.GeneralStatisticsTracking;

/**
 * @author Noctember
 */
public class XPDisplay extends DisplayItem {

    public XPDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        height = 10;
    }

    @Override
    public void draw(int x, double y, boolean config) {
        String render = "Daily XP: " + GeneralStatisticsTracking.dailyXP;
        ElementRenderer.draw(x, y, render);
        width = config ? ElementRenderer.getFontRenderer().getStringWidth(render) : 0;
    }
}
