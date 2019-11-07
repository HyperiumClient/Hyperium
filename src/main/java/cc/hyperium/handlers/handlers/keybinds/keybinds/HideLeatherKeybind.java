package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.utils.ChatColor;
import org.lwjgl.input.Keyboard;

public class HideLeatherKeybind extends HyperiumBind {

    public HideLeatherKeybind() {
        super("Toggle Leather Armor", Keyboard.KEY_F);
    }

    @Override
    public void onPress() {
        Settings.HIDE_LEATHER_ARMOR = !Settings.HIDE_LEATHER_ARMOR;
        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Leather armor is now " +
            (Settings.HIDE_LEATHER_ARMOR ? ChatColor.GREEN + "shown" : ChatColor.RED + "hidden") + ChatColor.WHITE + ".");
    }
}
