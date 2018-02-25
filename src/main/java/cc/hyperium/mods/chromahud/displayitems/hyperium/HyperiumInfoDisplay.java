package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.Metadata;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;

import java.util.ArrayList;
import java.util.List;

public class HyperiumInfoDisplay extends DisplayItem {
    public HyperiumInfoDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }

    @Override
    public Dimension draw(int x, double y, boolean config) {
        List<String> list = new ArrayList<>();
        list.add("Client: " + Metadata.getModid());
        list.add("Version " + Metadata.getVersion());
        ElementRenderer.draw(x, y, list);
        return new Dimension(config ? ElementRenderer.maxWidth(list) : 0, list.size() * 10);
    }
}
