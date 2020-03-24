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
import cc.hyperium.utils.BetterJsonObject;
import net.minecraft.client.Minecraft;

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
    // Reload the Minecraft key file, since this seems to fix it.
    Minecraft.getMinecraft().gameSettings.loadOptions();
  }

  public BetterJsonObject getKeyBindJson() {
    return keyBindJson;
  }

  private boolean exists(File file) {
    return file != null && Files.exists(Paths.get(file.getPath()));
  }

}
