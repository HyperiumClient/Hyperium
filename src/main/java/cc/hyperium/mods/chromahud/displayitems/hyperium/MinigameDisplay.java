package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.hgames.HGames;
import cc.hyperium.utils.JsonHolder;

public class MinigameDisplay extends DisplayItem {

    public MinigameDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }

    @Override
    public void draw(int x, double y, boolean config) {
        ((HGames) Hyperium.INSTANCE.getModIntegration().getHGames()).render(this, x, y, config);
    }

}
