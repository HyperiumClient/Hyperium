package cc.hyperium.mods.browser.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.mods.browser.gui.GuiBrowser;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

/**
 * @author Koding
 */
public class BrowserBind extends HyperiumBind {

    public BrowserBind() {
        super("Open Browser", Keyboard.KEY_G);
    }

    @Override
    public void onPress() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiBrowser)
            return;
        Hyperium.INSTANCE.getModIntegration().getBrowserMod().showBrowser();
    }
}
