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

package cc.hyperium.mods.togglechat.config;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.togglechat.ToggleChatMod;
import cc.hyperium.mods.togglechat.toggles.ToggleBase;
import cc.hyperium.utils.BetterJsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ToggleChatConfig {

    private final ToggleChatMod theMod;
    private final File toggleFile;
    private BetterJsonObject toggleJson = new BetterJsonObject();

    public ToggleChatConfig(ToggleChatMod theMod, File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }

        this.theMod = theMod;
        toggleFile = new File(directory, "togglechat.json");

    }

    public void loadToggles() {
        if (exists(toggleFile)) {
            try {
                FileReader fileReader = new FileReader(toggleFile);
                BufferedReader reader = new BufferedReader(fileReader);
                toggleJson = new BetterJsonObject(reader.lines().collect(Collectors.joining()));
                fileReader.close();
                reader.close();
            } catch (Exception ex) {
                log("Could not read toggles properly, saving.");
                saveToggles();
            }

            theMod.getToggleHandler().getToggles().values().forEach(base ->
                base.setEnabled(toggleJson.has("show" + base.getName().replace(" ",
                    "_")) && toggleJson.get("show" + base.getName().replace(" ", "_")).getAsBoolean()));
        } else {
            saveToggles();
        }
    }

    public void saveToggles() {
        try {
            if (!toggleFile.getParentFile().exists()) {
                toggleFile.getParentFile().mkdirs();
            }

            toggleFile.createNewFile();
            theMod.getToggleHandler().getToggles().values().forEach(base -> toggleJson.addProperty("show" + base.getName().replace(" ", "_"), base.isEnabled()));
            toggleJson.writeToFile(toggleFile);
        } catch (Exception ex) {
            log("Could not save toggles.");
            ex.printStackTrace();
        }
    }

    private boolean exists(File file) {
        return Files.exists(Paths.get(file.getPath()));
    }

    private void log(String message, Object... replace) {
        Hyperium.LOGGER.info("ToggleChat {} {}", message, replace);
    }
}
