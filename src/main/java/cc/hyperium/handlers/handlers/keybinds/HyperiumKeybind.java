package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class HyperiumKeybind {

  private String description, category;
  private int keyCode, defaultKeyCode;
  private boolean conflicted, pressed;

  public HyperiumKeybind(String description, int defaultKeyCode, KeyType category) {
    this(description, defaultKeyCode, category.keyCategory);
  }

  public HyperiumKeybind(String description, int defaultKeyCode, String category) {
    this.description = description;
    this.keyCode = defaultKeyCode;
    this.defaultKeyCode = defaultKeyCode;
    this.category = category;
  }

  public void onPress() {
    pressed = true;
  }

  public void onRelease() {
    pressed = false;
  }

  public String getDescription() {
    return description;
  }

  public String getCategory() {
    return category;
  }

  public int getKeyCode() {
    return keyCode;
  }

  public int getDefaultKeyCode() {
    return defaultKeyCode;
  }

  public KeyBinding toKeyBind() {
    return new KeyBinding(description, keyCode,
      "keys.categories.hyperium" + (category.isEmpty() ? "" : "." + category), defaultKeyCode);
  }

  public void setKeyCode(int keyCode) {
    this.keyCode = keyCode;
  }

  public boolean wasPressed() {
    return true;
  }

  public boolean isPressed() {
    return pressed;
  }

  public void setPressed(boolean pressed) {
    this.pressed = pressed;
  }

  public void detectConflicts() {
    conflicted = false;

    for (HyperiumKeybind value : Hyperium.INSTANCE.getHandlers().getKeybindHandler()
      .getKeybinds().values()) {
      // Make sure it's not this key we're checking.
      if (value.getDescription().equalsIgnoreCase(this.getDescription())) continue;
      // Make sure it's not nothing.
      if (value.getKeyCode() == 0) continue;
      // Check to see if the keybind conflicts with it.
      if (value.getKeyCode() == this.getKeyCode()) {
        // Set it to true, and don't iterate through more since we've already found a conflict.
        conflicted = true;
        return;
      }
    }
  }

  public boolean isConflicted() {
    return conflicted;
  }

  public enum KeyType {

    COSMETIC("cosmetics"), GUI("gui"), TEST("test"),
    UTIL("util"), NONE(""), CT("ct");

    // The suffix to add to the keys.hyperium string
    final String keyCategory;

    KeyType(String keyCategory) {
      this.keyCategory = keyCategory;
    }

  }


}
