/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.GameShutDownEvent;
import cc.hyperium.handlers.handlers.keybinds.keybinds.*;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import cc.hyperium.internal.addons.IAddon;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;

public class HyperiumKeybindHandler {

  private Map<String, HyperiumKeybind> keybinds = new HashMap<>();
  private KeybindConfiguration config;

  public HyperiumKeybindHandler() {
    // Register all the keybinds, regardless of the environment.
    registerBinds(new ArmWaveKeybind(), new DabKeybind(), new FlipKeybind(),
      new FlossKeybind(), new FriendsKeybind(), new GuiDanceKeybind(),
      new GuiKeybind(), new HideLeatherKeybind(), new NamesKeybind(),
      new RearCamKeybind(), new TogglePerspectiveKeybind(), new ToggleSprintKeybind(),
      new TPoseKeybind(), new TwerkDanceKeybind(), new UploadScreenshotKeybind(),
      new ViewStatsKeybind());

    // Register the debug key if this is a developer environment.
    if (Hyperium.INSTANCE.isDevEnv())
      registerBinds(new DebugKeybind());

    // Register a zoom key if Optifine is not detected.
    if (!Hyperium.INSTANCE.isOptifineInstalled())
      registerBinds(new ZoomKeybind());

    // Init & load the config.
    config = new KeybindConfiguration(this);
    config.load();

    // Register all the keybinds in GameSettings after the config has been loaded
    // so that the user can have their keycodes.
    registerKeybindings();
  }

  public void registerBinds(HyperiumKeybind... keyBinds) {
    for (HyperiumKeybind keyBind : keyBinds)
      this.keybinds.putIfAbsent(keyBind.getDescription(), keyBind);
  }

  public void registerKeybindings() {
    for (HyperiumKeybind bind : keybinds.values()) {
      // We should not register any keybinds more than just once
      if (!bind.registered) {
        // Add the key to the `allKeys` map, so that it shows in GuiControl, and to the keyBindings list.
        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.add(Minecraft.getMinecraft().gameSettings.keyBindings, bind.toKeyBind());
        bind.registered = true;
      }
    }
  }

  public void handleKey(int keyCode, boolean pressed) {
    // Make sure the game has focus, and there's no GUI open.
    if (!Minecraft.getMinecraft().inGameHasFocus
      && Minecraft.getMinecraft().currentScreen != null) {
      return;
    }

    // Loop through all keybinds.
    for (HyperiumKeybind keyBind : keybinds.values()) {
      // Check if the keybind is set to the current code.
      if (keyBind.getKeyCode() == keyCode) {
        // If it's pressed, execute onPress
        if (pressed) {
          keyBind.onPress();
        }
        // Otherwise, execute onRelease
        else {
          keyBind.onRelease();
        }
        keyBind.setPressed(pressed);
      }
    }
  }

  public void onHold() {
    for (HyperiumKeybind keyBind : keybinds.values()) {
      if (keyBind.isPressed()) keyBind.onHold();
    }
  }

  public Map<String, HyperiumKeybind> getKeybinds() {
    return keybinds;
  }

  @InvokeEvent
  public void onGameShutdown(GameShutDownEvent event) {
    // Save the config on shutdown.
    config.save();
  }

  public void setBind(String key, int keyCode) {
    // Make sure that we handle this key.
    if (!keybinds.containsKey(key)) {
      return;
    }

    // Set the bind, and then save the config.
    HyperiumKeybind keybind = keybinds.get(key);
    keybind.setKeyCode((keyCode != -1 ? keyCode : keybind.getDefaultKeyCode()));
    config.save();
  }

  public HyperiumKeybind getBind(String keyDesc) {
    return keybinds.get(keyDesc);
  }

  public KeybindConfiguration getConfig() {
    return config;
  }

  public void releaseAllKeybinds() {
    for (HyperiumKeybind bind : keybinds.values()) {
      if (bind.isPressed())
        bind.onRelease();
    }
  }

}
