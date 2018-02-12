package com.hcc.chromahud;

import com.google.gson.JsonArray;
import com.hcc.chromahud.api.DisplayItem;
import com.hcc.chromahud.gui.GeneralConfigGui;
import com.hcc.utils.JsonHolder;

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
        suggestedConfigurationFile = new File("hcc/displayconfig.json");
        ChromaHUDApi.getInstance();
        ChromaHUDApi.getInstance().register(new DefaultChromaHudParser());

        setup();



    }


    public void setup() {
        JsonHolder data = new JsonHolder();
        try {
            if(!suggestedConfigurationFile.exists()) {
                if(!suggestedConfigurationFile.getParentFile().exists())
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
//        getDisplayElements().removeIf((displayElement) -> displayElement.getDisplayItems().size() == 0);
        for (DisplayElement element : getDisplayElements()) {
//            if (element.getDisplayItems().size() == 0)
//                continue;
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
