package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.utils.UUIDUtil;
import org.lwjgl.input.Keyboard;

public class TwerkDanceKeybind extends HyperiumBind {
    public TwerkDanceKeybind() {
        super("Twerk..", Keyboard.KEY_Y);
    }

    @Override
    public void onPress() {
        Hyperium.INSTANCE.getHandlers().getTwerkDance().getStates().put(UUIDUtil.getClientUUID(), System.currentTimeMillis());
        Hyperium.INSTANCE.getHandlers().getTwerkDance().startAnimation(UUIDUtil.getClientUUID());
    }
}
