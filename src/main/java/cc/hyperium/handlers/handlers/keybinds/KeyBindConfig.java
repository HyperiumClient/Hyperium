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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * KeyBinding config, basically just a modified ToggleChat config
 *
 * @author boomboompower
 */
@SuppressWarnings("ResultOfMethodCallIgnored") // Suppress because we don't care
public class KeyBindConfig {

    private final KeyBindHandler handler;
    private final File keybindFile;
    private BetterJsonObject keyBindJson = new BetterJsonObject();

    KeyBindConfig(KeyBindHandler handler, File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }

        this.handler = handler;
        keybindFile = new File(directory, "keybinds.json");
    }

    public BetterJsonObject getKeyBindJson() {
        return keyBindJson;
    }

    public void save() {
        if (!exists(keybindFile)) {
            keybindFile.getParentFile().mkdirs();
        }

        try {
            keybindFile.createNewFile();
            handler.getKeybinds().values().forEach(base -> keyBindJson.addProperty(base.getRealDescription(), base.getKeyCode()));
            keyBindJson.writeToFile(keybindFile);
        } catch (IOException ex) {
            Hyperium.LOGGER.warn("An error occured while saving the Hyperium KeyBlinds, this is not good.");
        }
    }

    public void load() {
        if (exists(keybindFile)) {
            try {
                FileReader fileReader = new FileReader(keybindFile);
                BufferedReader reader = new BufferedReader(fileReader);
                keyBindJson = new BetterJsonObject(reader.lines().collect(Collectors.joining()));
                fileReader.close();
                reader.close();
            } catch (Exception ex) {
                // Error occured while loading
                save();
                return;
            }

            handler.getKeybinds().values().forEach(bind -> bind.setKeyCode(keyBindJson.optInt(bind.getRealDescription(), bind.getDefaultKeyCode())));
        } else {
            // Config file doesn't exist, yay!
            save();
        }
    }

    public void attemptKeyBindLoad(HyperiumBind bind) {
        if (keyBindJson.has(bind.getRealDescription())) {
            bind.setKeyCode(keyBindJson.optInt(bind.getRealDescription(), bind.getDefaultKeyCode()));
        }
    }

    private boolean exists(File file) {
        return file != null && Files.exists(Paths.get(file.getPath()));
    }
}
