package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.utils.UUIDUtil;
import org.lwjgl.input.Keyboard;

public class FortniteDefaultDanceKeybind extends HyperiumBind {
    public FortniteDefaultDanceKeybind() {
        super("Do the default Fortnite dance", Keyboard.KEY_U);
    }

    @Override
    public void onPress() {
        Hyperium.INSTANCE.getHandlers().getFortniteDefaultDance().getStates().put(UUIDUtil.getClientUUID(), System.currentTimeMillis());
        Hyperium.INSTANCE.getHandlers().getFortniteDefaultDance().startAnimation(UUIDUtil.getClientUUID());
    }
}
