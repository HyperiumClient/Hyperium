package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

/**
 * @author KodingKing
 */
public class RearCamKeybind extends HyperiumBind {

    public RearCamKeybind() {
        super("Rear Cam", Keyboard.KEY_R);
    }

    @Override
    public void onPress() {
        Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
    }

    @Override
    public void onRelease() {
        Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
    }
}
