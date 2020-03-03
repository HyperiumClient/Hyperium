package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.handlers.handlers.keybinds.HyperiumKeybind;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ZoomKeybind extends HyperiumKeybind {
  public ZoomKeybind() {
    super("Zoom", Keyboard.KEY_C, KeyType.UTIL);
  }

  @Override
  public void onPress() {
    Minecraft.getMinecraft().gameSettings.fovSetting -= 50f;
  }

  @Override
  public void onRelease() {
    Minecraft.getMinecraft().gameSettings.fovSetting += 50f;
  }
}
