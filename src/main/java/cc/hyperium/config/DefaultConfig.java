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


import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sk1er
 */
public class DefaultConfig {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private JsonObject config = new JsonObject();
    private List<Object> configObjects = new ArrayList<>();
    private File file;

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
        } catch (Exception e) {
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
        try {
            loadToClassObject(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadToClassObject(Object object) throws IllegalAccessException {
        Class<?> aClass = object.getClass();
        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ConfigOpt.class)) {
                if (config.has(aClass.getName())) {
                    JsonObject tmp = config.get(aClass.getName()).getAsJsonObject();
                    if (tmp.has(field.getName())) {
                        JsonElement jsonElement = tmp.get(field.getName());
                        if (field.getType().isAssignableFrom(int.class)) {
                            field.set(object, jsonElement.getAsInt());
                        } else if (field.getType().isAssignableFrom(String.class)) {
                            field.set(object, jsonElement.getAsString());
                        } else if (field.getType().isAssignableFrom(boolean.class)) {
                            field.set(object, jsonElement.getAsBoolean());
                        } else if (field.getType().isAssignableFrom(double.class)) {
                            field.set(object, jsonElement.getAsDouble());
                        } else if (field.getType().isAssignableFrom(long.class)) {
                            field.set(object, jsonElement.getAsLong());
                        } else if (field.getType().isAssignableFrom(float.class)) {
                            field.set(object, jsonElement.getAsFloat());
                        }
                    }
                }
            }
        }
    }

    private void saveToJsonFromRamObject(Object o) {
        try {
            loadToJson(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadToJson(Object object) throws IllegalAccessException {
        Class<?> aClass = object.getClass();
        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(ConfigOpt.class) != null) {
                if (!config.has(aClass.getName())) {
                    config.add(aClass.getName(), new JsonObject());
                }
                JsonObject classObject = config.get(aClass.getName()).getAsJsonObject();
                if (field.getType().isAssignableFrom(int.class)) {
                    classObject.addProperty(field.getName(), field.getInt(object));
                } else if (field.getType().isAssignableFrom(String.class)) {
                    classObject.addProperty(field.getName(), (String) field.get(object));
                } else if (field.getType().isAssignableFrom(boolean.class)) {
                    classObject.addProperty(field.getName(), field.getBoolean(object));
                } else if (field.getType().isAssignableFrom(double.class)) {
                    classObject.addProperty(field.getName(), field.getDouble(object));
                } else if (field.getType().isAssignableFrom(long.class)) {
                    classObject.addProperty(field.getName(), field.getLong(object));
                } else if (field.getType().isAssignableFrom(float.class)) {
                    classObject.addProperty(field.getName(), field.getFloat(object));
                }
            }
        }
    }

    public JsonObject getConfig() {
        return config;
    }
}