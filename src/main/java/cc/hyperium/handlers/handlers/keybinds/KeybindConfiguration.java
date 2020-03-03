package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.utils.BetterJsonObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Keybinding config, basically just the original KeyBindConfig file, which was just a modified
 * ToggleChat config file basically.
 *
 * @author littlemissantivirus
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class KeybindConfiguration {

  private HyperiumKeybindHandler keybindHandler;
  private File keybindFile;
  private BetterJsonObject keyBindJson = new BetterJsonObject();

  public KeybindConfiguration(HyperiumKeybindHandler keybindHandler) {
    this.keybindHandler = keybindHandler;
    // Avoid conflict with older versions/configs.
    keybindFile = new File(Hyperium.folder, "keybinds.json");
  }

  public void save() {
    if (!exists(keybindFile)) {
      keybindFile.getParentFile().mkdirs();
    }

    try {
      keybindFile.createNewFile();
      for (HyperiumKeybind bind : keybindHandler.getKeybinds().values()) {
        keyBindJson.addProperty(bind.getDescription(), bind.getKeyCode());
      }
      keyBindJson.writeToFile(keybindFile);
    } catch (IOException ex) {
      Hyperium.LOGGER.warn(
        "An error occurred while saving the Hyperium KeyBinds, uh oh...");
    }
  }

  public void load() {
    if (!exists(keybindFile)) {
      save();
      return;
    }

    try {
      FileReader fr = new FileReader(keybindFile);
      BufferedReader br = new BufferedReader(fr);
      keyBindJson = new BetterJsonObject(br.lines().collect(Collectors.joining()));
      fr.close();
      br.close();
    } catch (Exception ex) {
      save();
      return;
    }

    for (HyperiumKeybind bind : keybindHandler.getKeybinds().values()) {
      bind.setKeyCode(keyBindJson.optInt(bind.getDescription(), bind.getDefaultKeyCode()));
    }
  }

  public BetterJsonObject getKeyBindJson() {
    return keyBindJson;
  }

  private boolean exists(File file) {
    return file != null && Files.exists(Paths.get(file.getPath()));
  }

}
