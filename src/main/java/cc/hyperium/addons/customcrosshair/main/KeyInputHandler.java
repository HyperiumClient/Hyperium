package cc.hyperium.addons.customcrosshair.main;

import cc.hyperium.addons.customcrosshair.gui.GuiEditCrosshair;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeypressEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.omg.CORBA.CustomMarshal;

public class KeyInputHandler {
    @InvokeEvent
    public void onKeyInput(final KeypressEvent event) {
        if(CustomCrosshairAddon.getCrosshairMod().getCrosshair().getEnabled()) {
            if (Keyboard.getKeyIndex(CustomCrosshairAddon.getCrosshairMod().getGuiKeyBind()) == event.getKey()) {
                if (Minecraft.getMinecraft().currentScreen == null) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiEditCrosshair());
                }
            }
        }
    }
}
