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

import com.google.common.base.Stopwatch;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hcc.HCC;
import com.hcc.addons.annotations.Addon;
import com.hcc.addons.loader.AddonLoaderStrategy;
import com.hcc.event.EventBus;
import com.hcc.exceptions.HCCException;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HCCAddonBootstrap {


    /**
     *  Addons directory
     */
    private static final File modDirectory = new File(HCC.folder, "addons");

    /**
     * Addons file that is found
     */
    private ArrayList<File> addons;

    /**
     * default constructor
     * @throws HCCException when error occurs
     */
    public HCCAddonBootstrap() throws HCCException {
        if (!modDirectory.mkdirs() && !modDirectory.exists()) {
            throw new HCCException("Unable to create addon directory!");
        }

        this.addons = Arrays
                .stream(modDirectory.listFiles())
                .filter(file -> file.getName().endsWith(".jar"))
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public void loadInternalAddon() throws Exception {
        try {
            JsonParser parser = new JsonParser();
            URL resource = HCC.class.getClassLoader().getResource("addon.json");
            String lines = IOUtils.toString(resource.openStream());
            JsonObject json = parser.parse(lines).getAsJsonObject();

            if (!json.has("version") && !json.has("name") && !json.has("main")) {
                throw new HCCException("Invalid addon (addon.json does not exist or invalid)");
            }
            Class<?> addonMain = Class.forName(json.get("main").getAsString());
            Object instance = addonMain.newInstance();
            AddonLoaderStrategy.assignInstances(instance);
            for (Annotation annotation : addonMain.getAnnotations()) {
                if (annotation instanceof Addon) {
                    // do whatever with Addon annotation?
                    EventBus.INSTANCE.register(instance);
                    break;
                }
            }
            System.out.println("Successfully loaded " + json.get("main").getAsString() + " from workspace.");
        } catch (Exception e) {
        }
    }

    /**
     * load addons from folder using the AddonLoaderStrategy
     * @param loader Addon loader
     */
    public void loadAddons(AddonLoaderStrategy loader) {
        Stopwatch benchmark = Stopwatch.createStarted();
        HCC.logger.info("Starting to load addons...");
        for (File addon : this.addons) {
            try {
                loader.load(addon);
                HCC.logger.info("Loaded {}", addon.getName());
            } catch (Exception e) {
                HCC.logger.error("Could not load {}!", addon.getName());
                e.printStackTrace();
            }
        }
        HCC.logger.debug("Finished loading all addons in {}.", benchmark);

    }

}
