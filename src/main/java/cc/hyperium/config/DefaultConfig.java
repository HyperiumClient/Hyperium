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

package cc.hyperium.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sk1er
 */
public class DefaultConfig {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final JsonParser parser = new JsonParser();

    private JsonObject config = new JsonObject();
    private final List<Object> configObjects = new ArrayList<>();
    private final File file;

    public DefaultConfig(File configFile) {
        this.file = configFile;
        try {
            if (configFile.exists()) {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                    builder.append(line);

                String done = builder.toString();
                config = new JsonParser().parse(done).getAsJsonObject();
            } else {
                config = new JsonObject();
                saveFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile() {
        try {
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(gson.toJson(config));
            bw.close();
            fw.close();
        } catch (Exception ignored) {
        }
    }

    public void save() {
        for (Object o : configObjects)
            saveToJsonFromRamObject(o);
        saveFile();
    }

    public Object register(Object object) {
        configObjects.add(object);
        loadToClass(object);
        return object;
    }

    private void loadToClass(Object o) {
        loadToClassObject(o);
    }

    private void loadToClassObject(Object object) {
        Class<?> c = object.getClass();
        if (!config.has(c.getName())) config.add(c.getName(), new JsonObject());
        Arrays.stream(c.getDeclaredFields()).filter(f -> f.isAnnotationPresent(ConfigOpt.class) && config.has(c.getName())).forEach(f -> {
            f.setAccessible(true);
            ConfigOpt co = f.getAnnotation(ConfigOpt.class);
            JsonObject tmp = config.get(c.getName()).getAsJsonObject();
            if (!co.alt().isEmpty() && config.has(co.alt().split(";")[0]) && !tmp.has(f.getName())) {
                JsonObject ot = config.get(co.alt().split(";")[0]).getAsJsonObject();
                if(ot.has(co.alt().split(";")[1]))
                    tmp.add(f.getName(), ot.get(co.alt().split(";")[1]));
            }
            if (tmp.has(f.getName())) {
                try {
                    f.set(object, gson.fromJson(tmp.get(f.getName()), f.getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveToJsonFromRamObject(Object o) {
        loadToJson(o);
    }

    private void loadToJson(Object object) {
        Class<?> c = object.getClass();
        Arrays.stream(c.getDeclaredFields()).filter(f -> f.isAnnotationPresent(ConfigOpt.class) && config.has(c.getName())).forEach(f -> {
            f.setAccessible(true);
            JsonObject classObject = config.get(c.getName()).getAsJsonObject();
            try {
                classObject.add(f.getName(), gson.toJsonTree(f.get(object), f.getType()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public JsonObject getConfig() {
        return config;
    }
}