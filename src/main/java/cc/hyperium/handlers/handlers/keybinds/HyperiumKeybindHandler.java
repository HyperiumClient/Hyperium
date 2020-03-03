package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.GameShutDownEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.keybinds.keybinds.*;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

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
    for (HyperiumKeybind keyBind : keyBinds) {
      this.keybinds.putIfAbsent(keyBind.getDescription(), keyBind);
    }
  }

  public void registerKeybindings() {
    for (HyperiumKeybind bind : keybinds.values()) {
      // Add the key to the `allKeys` map, so that it shows in GuiControl.
      Minecraft.getMinecraft().gameSettings.allKeys.add(bind.toKeyBind());
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
          keyBind.setPressed(true);
        }
        // Otherwise, execute onRelease
        else {
          keyBind.onRelease();
          keyBind.setPressed(false);
        }
      }
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

  public void refreshKeys() {
    for (HyperiumKeybind bind : keybinds.values()) {
      KeyBinding keyBinding = bind.toKeyBind();
      for (KeyBinding allKey : Minecraft.getMinecraft().gameSettings.allKeys) {
        if (allKey.getKeyDescription().equalsIgnoreCase(keyBinding.getKeyDescription())) {
          allKey.setKeyCode(keyBinding.getKeyCode());
        }
      }
    }
  }
}
