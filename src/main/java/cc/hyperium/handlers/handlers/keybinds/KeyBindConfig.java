/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
        this.keybindFile = new File(directory, "keybinds.json");
    }

    public BetterJsonObject getKeyBindJson() {
        return keyBindJson;
    }

    public void save() {
        if (!exists(this.keybindFile)) {
            this.keybindFile.getParentFile().mkdirs();
        }

        try {
            this.keybindFile.createNewFile();

            for (HyperiumBind base : this.handler.getKeybinds().values()) {
                this.keyBindJson.addProperty(base.getRealDescription(), base.getKeyCode());
            }

            this.keyBindJson.writeToFile(this.keybindFile);
        } catch (IOException ex) {
            Hyperium.LOGGER.warn("An error occured while saving the Hyperium KeyBlinds, this is not good.");
        }
    }

    public void load() {
        if (exists(this.keybindFile)) {
            try {
                FileReader fileReader = new FileReader(this.keybindFile);
                BufferedReader reader = new BufferedReader(fileReader);
                StringBuilder builder = new StringBuilder();

                String current;
                while ((current = reader.readLine()) != null) {
                    builder.append(current);
                }
                this.keyBindJson = new BetterJsonObject(builder.toString());
            } catch (Exception ex) {
                // Error occured while loading
                save();
                return;
            }

            for (HyperiumBind bind : this.handler.getKeybinds().values()) {
                bind.setKeyCode(this.keyBindJson.optInt(bind.getRealDescription(), bind.getDefaultKeyCode()));
            }
        } else {
            // Config file doesn't exist, yay!
            save();
        }
    }

    public void attemptKeyBindLoad(HyperiumBind bind) {
        if (this.keyBindJson.has(bind.getRealDescription())) {
            bind.setKeyCode(this.keyBindJson.optInt(bind.getRealDescription(), bind.getDefaultKeyCode()));
        }
    }

    private boolean exists(File file) {
        return file != null && Files.exists(Paths.get(file.getPath()));
    }
}
