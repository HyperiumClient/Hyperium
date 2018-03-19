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

package cc.hyperium.mods.skinchanger.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cc.hyperium.mods.skinchanger.SkinChangerMod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SkinChangerConfig {
    
    private JsonObject configJson = new JsonObject();
    private SkinChangerMod mod;
    private File configFile;
    
    private String skinName = "";
    private String ofCapeName = "";
    private boolean usingCape = false;
    private boolean experimental = false;
    
    public SkinChangerConfig(SkinChangerMod mod, File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    
        this.mod = mod;
    
        this.configFile = new File(directory, "skinchanger.json");
    }

    public void load() {
        if (exists()) {
            try {
                FileReader fileReader = new FileReader(this.configFile);
                BufferedReader reader = new BufferedReader(fileReader);
                StringBuilder builder = new StringBuilder();

                String current;
                while ((current = reader.readLine()) != null) {
                    builder.append(current);
                }
                this.configJson = new JsonParser().parse(builder.toString()).getAsJsonObject();
            } catch (Exception ex) {
                log("Could not read log properly, saving.", this.configFile.getName());
                save();
            }
            this.skinName = this.configJson.has("skinname") ? this.configJson.get("skinname").getAsString() : "";
            this.usingCape = this.configJson.has("usingcape") && this.configJson.get("usingcape").getAsBoolean();
            this.experimental = this.configJson.has("experimental") && this.configJson.get("experimental").getAsBoolean();
            if (this.configJson.has("experimental") && this.configJson.get("experimental").getAsBoolean() && this.configJson.has("ofCapeName")) {
                this.ofCapeName = this.configJson.get("ofCapeName").getAsString();
            }
        } else {
            log("Config doesn\'t exist. Saving.", this.configFile.getName());
            save();
        }
    }

    public void save() {
        try {
            this.configFile.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.configFile));
            this.configJson.addProperty("skinname", this.skinName);
            this.configJson.addProperty("usingcape", this.usingCape);
            this.configJson.addProperty("experimental", this.experimental);

            if (this.experimental) {
                this.configJson.addProperty("ofCapeName", this.ofCapeName);
            }

            bufferedWriter.write(this.configJson.toString());
            bufferedWriter.close();
            log("Saved config.", this.configFile.getName());
        } catch (Exception ex) {
            log("Could not save.", this.configFile.getName());
            ex.printStackTrace();
        }
    }

    public boolean exists() {
        return Files.exists(Paths.get(this.configFile.getPath()));
    }

    public File getConfigFile() {
        return this.configFile;
    }
    
    public String getSkinName() {
        return this.skinName;
    }
    
    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }
    
    public String getOfCapeName() {
        return this.ofCapeName;
    }
    
    public void setOfCapeName(String ofCapeName) {
        this.ofCapeName = ofCapeName;
    }
    
    public boolean isExperimental() {
        return this.experimental;
    }
    
    public void setExperimental(boolean experimental) {
        this.experimental = experimental;
    }
    
    public boolean isUsingCape() {
        return this.usingCape;
    }
    
    public void setUsingCape(boolean usingCape) {
        this.usingCape = usingCape;
    }
    
    public SkinChangerMod getMod() {
        return this.mod;
    }
    
    protected void log(String message, Object... replace) {
        System.out.println(String.format("[%s] " + message, replace));
    }
}
