package cc.hyperium.mods.autofriend.confog;

import cc.hyperium.config.Category;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.ToggleSetting;

public class AutofriendConfig {

    @ConfigOpt
    @ToggleSetting(name = "Enabled", category = Category.AUTOFRIEND, mods = true)
    public static boolean toggle = true;

    @ConfigOpt
    @ToggleSetting(name = "Show Friend Messages", category = Category.AUTOFRIEND, mods = true)
    public static boolean messages = true;

}
