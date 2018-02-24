/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.chromahud;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.chromahud.api.ButtonConfig;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.chromahud.gui.GeneralConfigGui;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;
import net.minecraft.client.gui.GuiButton;

import java.io.*;
import java.util.List;

public class ChromaHUD {
    public static final String MODID = "ChromaHUD";
    public static final String VERSION = "3.0";
    private File suggestedConfigurationFile;
    //TODO
    private boolean enabled = true;

    public ChromaHUD() {
        init();
    }


    private void init() {
        suggestedConfigurationFile = new File(Hyperium.folder, "/displayconfig.json");
        ChromaHUDApi.getInstance();
        ChromaHUDApi.getInstance().register(new DefaultChromaHUDParser());
        ChromaHUDApi.getInstance().register(new HyperiumChromaHudParser());
        ChromaHUDApi.getInstance().registerButtonConfig("SCOREBOARD", new ButtonConfig((guiButton, displayItem) -> {
            displayItem.getData().put("numbers",!displayItem.getData().optBoolean("numbers"));
        }, new GuiButton(0, 0, 0, "Toggle Number"), (guiButton, displayItem) -> {

        }));
        setup();
        EventBus.INSTANCE.register(new ElementRenderer(this));


    }


    public void setup() {
        JsonHolder data = new JsonHolder();
        try {
            if (!suggestedConfigurationFile.exists()) {
                if (!suggestedConfigurationFile.getParentFile().exists())
                    suggestedConfigurationFile.getParentFile().mkdirs();
                saveState();
            }
            FileReader fr = new FileReader(suggestedConfigurationFile);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null)
                builder.append(line);

            String done = builder.toString();
            data = new JsonHolder(done);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChromaHUDApi.getInstance().post(data);
    }

    public List<DisplayElement> getDisplayElements() {
        return ChromaHUDApi.getInstance().getElements();
    }


    public GeneralConfigGui getConfigGuiInstance() {
        return new GeneralConfigGui(this);
    }

    /*
    Saves current state of all elements to file
     */
    public void saveState() {
        JsonHolder master = new JsonHolder();
        master.put("enabled", enabled);
        JsonArray elementArray = new JsonArray();
        master.put("elements", elementArray);
        for (DisplayElement element : getDisplayElements()) {
            JsonHolder tmp = element.getData();
            JsonArray items = new JsonArray();
            for (DisplayItem item : element.getDisplayItems()) {
                JsonHolder raw = item.getData();
                raw.put("type", item.getType());
                items.add(raw.getObject());
            }
            elementArray.add(tmp.getObject());

            tmp.put("items", items);
        }
        try {
            if (!suggestedConfigurationFile.exists())
                suggestedConfigurationFile.createNewFile();
            FileWriter fw = new FileWriter(suggestedConfigurationFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(master.toString());
            bw.close();
            fw.close();
        } catch (Exception e) {

        }

    }


}
