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

package cc.hyperium.addons.sidebar.config;

import cc.hyperium.addons.sidebar.gui.GuiSidebar;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Configuration {

    public boolean enabled = true;
    public int offsetX;
    public int offsetY;
    public float scale = 1.0f;
    public boolean redNumbers = true;
    public boolean shadow;
    public int rgb;
    public int alpha = 50;
    public boolean chromaEnabled;
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
