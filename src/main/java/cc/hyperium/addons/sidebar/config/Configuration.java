package cc.hyperium.addons.sidebar.config;

import cc.hyperium.addons.sidebar.gui.GuiSidebar;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Configuration {
    private Configuration() {
    }

    public boolean enabled = true;
    public int offsetX = 0;
    public int offsetY = 0;
    public float scale = 1.0f;
    public boolean redNumbers = true;
    public boolean shadow = false;
    public int rgb = 0;
    public int alpha = 50;
    public boolean chromaEnabled = false;
    public int chromaSpeed = 2;
    public GuiSidebar.ChromaType chromaType = GuiSidebar.ChromaType.ONE;

    public static Configuration load(File saveFile) throws IOException {
        if (!saveFile.isFile()) {
            saveFile.createNewFile();
            return new Configuration().save(saveFile);
        }
        return new GsonBuilder().setPrettyPrinting().create().fromJson(new FileReader(saveFile), Configuration.class);
    }

    public Configuration save(File saveFile) throws IOException {
        FileWriter writer = new FileWriter(saveFile);
        writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(this));
        writer.close();
        return this;
    }
}
