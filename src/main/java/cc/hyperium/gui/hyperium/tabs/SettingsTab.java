package cc.hyperium.gui.hyperium.tabs;

import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.gui.hyperium.components.CollapsibleTabComponent;

import java.util.Collections;

/*
 * Created by Cubxity on 30/08/2018
 */
public class SettingsTab extends AbstractTab {
    public SettingsTab(HyperiumMainGui gui) {
        super(gui, "Settings");
        components.add(new CollapsibleTabComponent(this, Collections.emptyList(), "General"));
    }
}
