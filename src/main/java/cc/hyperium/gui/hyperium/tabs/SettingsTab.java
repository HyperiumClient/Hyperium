package cc.hyperium.gui.hyperium.tabs;

import cc.hyperium.config.Settings;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.hyperium.components.*;

import java.util.Collections;

/*
 * Created by Cubxity on 30/08/2018
 */
public class SettingsTab extends AbstractTab {
    public SettingsTab(HyperiumMainGui gui) {
        super(gui, "Settings");
        try {
            components.add(new CollapsibleTabComponent(this, Collections.emptyList(), "General")
                    .addChild(new CollapsibleTabComponent(this, Collections.emptyList(), "Test child"))
                    .addChild(new LabelComponent(this, Collections.emptyList(), "Label 1"))
                    .addChild(new ToggleComponent(this, Collections.emptyList(), "Really really long label that will help me test and debug the wrapping when the GUI is too narrow and it would otherwise overflow", Settings.class.getField("FAST_CONTAINER"), null))
                    .addChild(new SelectorComponent(this, Collections.emptyList(), "Field name 4", Settings.class.getDeclaredField("PARTICLE_MODE"), null, () -> new String[]{
                            "OFF",
                            "PLAIN 1",
                            "PLAIN 2",
                            "CHROMA 1",
                            "CHROMA 2"
                    })));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        components.add(new CollapsibleTabComponent(this, Collections.emptyList(), "General 2")
                .addChild(new CollapsibleTabComponent(this, Collections.emptyList(), "Test child")));
    }
}
