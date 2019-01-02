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

package cc.hyperium.mods.togglechat.config;

import cc.hyperium.mods.togglechat.ToggleChatMod;
import cc.hyperium.mods.togglechat.toggles.ToggleBase;
import cc.hyperium.utils.BetterJsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ToggleChatConfig {

    private final ToggleChatMod theMod;
    private final File toggleFile;
    private BetterJsonObject toggleJson = new BetterJsonObject();

    public ToggleChatConfig(ToggleChatMod theMod, File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }

        this.theMod = theMod;
        this.toggleFile = new File(directory, "togglechat.json");

    }

    public void loadToggles() {
        if (exists(this.toggleFile)) {
            try {
                FileReader fileReader = new FileReader(this.toggleFile);
                BufferedReader reader = new BufferedReader(fileReader);
                StringBuilder builder = new StringBuilder();

                String current;
                while ((current = reader.readLine()) != null) {
                    builder.append(current);
                }
                this.toggleJson = new BetterJsonObject(builder.toString());
            } catch (Exception ex) {
                log("Could not read toggles properly, saving.");
                saveToggles();
            }

            for (ToggleBase base : this.
                theMod.
                getToggleHandler()
                .getToggles()
                .values()) {
                base.setEnabled(this.toggleJson.has("show" + base.getName().replace(" ", "_")) && this.toggleJson.get("show" + base.getName().replace(" ", "_")).getAsBoolean());
            }

        } else {
            saveToggles();
        }
    }

    public void saveToggles() {
        try {
            if (!this.toggleFile.getParentFile().exists()) {
                this.toggleFile.getParentFile().mkdirs();
            }

            this.toggleFile.createNewFile();
            FileWriter writer = new FileWriter(this.toggleFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            for (ToggleBase base : this.theMod.getToggleHandler().getToggles().values()) {
                this.toggleJson.addProperty("show" + base.getName().replace(" ", "_"), base.isEnabled());
            }

            this.toggleJson.writeToFile(this.toggleFile);
        } catch (Exception ex) {
            log("Could not save toggles.");
            ex.printStackTrace();
        }
    }

    private boolean exists(File file) {
        return Files.exists(Paths.get(file.getPath()));
    }

    private void log(String message, Object... replace) {
        System.out.println(String.format("[ToggleChatLite] " + message, replace));
    }
}
