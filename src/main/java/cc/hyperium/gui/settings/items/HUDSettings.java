package cc.hyperium.gui.settings.items;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;

import java.util.Arrays;

/*
 * by ConorTheDev
 */

public class HUDSettings extends SettingGui {

    @ConfigOpt
    public static boolean chromahud_toggle = false;

    private final DefaultConfig config;

    public HUDSettings(HyperiumGui previous) {
        super("HUD", previous);
        config = Hyperium.CONFIG;
        config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();

        SelectionItem<String> chromahudtoggle;
        settingItems.add(chromahudtoggle = new SelectionItem<>(0, getX(), getDefaultItemY(1), width - getX() * 2, "TOGGLE CHROMAHUD", i -> {
            ((SelectionItem) i).nextItem();
            chromahud_toggle = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        chromahudtoggle.addItems(Arrays.asList("ON", "OFF"));
        chromahudtoggle.setSelectedItem(chromahud_toggle ? "ON" : "OFF");
    }

    private int getDefaultItemY(int i) {
        return getY() + 25 + i * 15;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        config.save();
    }

}
