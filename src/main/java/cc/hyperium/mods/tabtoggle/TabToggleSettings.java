package cc.hyperium.mods.tabtoggle;

import cc.hyperium.config.Category;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.ToggleSetting;

public class TabToggleSettings {

    public static boolean TAB_TOGGLED = false;
    @ConfigOpt
    @ToggleSetting(name = "Tab Toggle", category = Category.TAB_TOGGLE, mods = true)
    public static boolean ENABLED = false;

}
