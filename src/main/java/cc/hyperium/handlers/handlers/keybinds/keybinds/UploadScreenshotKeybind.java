package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import org.lwjgl.input.Keyboard;

public class UploadScreenshotKeybind extends HyperiumBind {

    public UploadScreenshotKeybind() {
        super("Upload Screenshot", Keyboard.KEY_LSHIFT);
        this.conflictExempt = true;
    }
}
