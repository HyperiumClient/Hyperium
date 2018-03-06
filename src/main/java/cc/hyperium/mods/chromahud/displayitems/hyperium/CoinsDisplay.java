package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.statistics.GeneralStatisticsTracking;
import cc.hyperium.utils.JsonHolder;

/**
 * Created by mitchellkatz on 2/26/18. Designed for production use on Sk1er.club
 */
public class CoinsDisplay extends DisplayItem {


    public CoinsDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.height = 10;
    }

    @Override
    public void draw(int x, double y, boolean config) {

        String render = null;
        if (data.optInt("state") == 0) {
            render = "Daily Coins: " + GeneralStatisticsTracking.dailyCoins;
        }
        if (data.optInt("state") == 1) {
            render = "Monthly Coins: " + GeneralStatisticsTracking.monthlyCoins;
        }
        if (data.optInt("state") == 2) {
            render = "Lifetime Coins: " + GeneralStatisticsTracking.lifetimeCoins;
        }
        if (render == null) {
            render = "Error, " + data.optInt("type") + " invalid state";
        }
        ElementRenderer.draw(x, y, render);
        this.width =config ? ElementRenderer.getFontRenderer().getStringWidth(render) : 0;
    }
}
