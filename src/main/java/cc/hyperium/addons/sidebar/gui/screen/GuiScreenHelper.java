package cc.hyperium.addons.sidebar.gui.screen;

import cc.hyperium.utils.ChatColor;

public interface GuiScreenHelper {
    default String getSuffix(boolean enabled) {
        return enabled ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled");
    }
}
