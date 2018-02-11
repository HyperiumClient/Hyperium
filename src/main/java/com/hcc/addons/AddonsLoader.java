/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.addons;

import com.hcc.HCC;
import com.hcc.exceptions.HCCException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddonsLoader {
    private final Class<?>[] parameters = new Class[]{URL.class};
    private List<Addon> addons = new ArrayList<>();
    public void loadAll(File folder) {
        File[] files = folder.listFiles(File::isDirectory);
        assert files != null;
        Arrays.asList(files).forEach(this::load);
    }
    public void load(File f){
        AddonsDescription description;
        try {
           description = new AddonsDescription(f);
        } catch (HCCException e) {
            e.printStackTrace();
            HCC.logger.error("Failed to load addon: "+f.getAbsolutePath());
            return;
        }
        try{
            addURL(f.toURI().toURL());
        }catch (IOException e){
            e.printStackTrace();
            HCC.logger.error("Failed to load addon: "+f.getAbsolutePath());
            return;
        }
        Class<? extends Addon> addonClass;
        try {
            Class<?> addonMain = Class.forName(description.getMain());
            addonClass = addonMain.asSubclass(Addon.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            HCC.logger.error("Failed to load addon: "+f.getAbsolutePath()+", failed to load main class");
            return;
        }
        Addon addon;
        try {
            addon = addonClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            HCC.logger.error("Failed to load addon: "+f.getAbsolutePath()+", failed to call constructor");
            return;
        }
        addon.description = description;
        addons.add(addon);
        HCC.logger.info("Loading addon "+addon.getDescription().getName()+" "+addon.getDescription().getVersion());
        try {
            addon.onLoad();
            HCC.logger.info("Successfully loaded "+addon.getDescription().getName());
        }catch (Exception e){
            e.printStackTrace();
            HCC.logger.error("Failed to load addon: "+addon.getDescription().getName());
        }

    }

    public void unload(Addon addon){
        addon.onDisable();
    }

    public void unloadAll(){
        addons.forEach(this::unload);
    }

    private void addURL(URL u) throws IOException {
        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL",parameters);
            method.setAccessible(true);
            method.invoke(sysloader, u);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }
}
