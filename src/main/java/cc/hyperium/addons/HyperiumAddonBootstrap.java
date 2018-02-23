/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
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

package cc.hyperium.addons;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.annotations.Addon;
import cc.hyperium.addons.loader.AddonLoaderStrategy;
import com.google.common.base.Stopwatch;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cc.hyperium.event.EventBus;
import cc.hyperium.exceptions.HyperiumException;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HyperiumAddonBootstrap {


    /**
     * Addons directory
     */
    private static final File modDirectory = new File(Hyperium.folder, "addons");

    /**
     * Addons file that is found
     */
    private ArrayList<File> addons;

    /**
     * default constructor
     *
     * @throws HyperiumException when error occurs
     */
    public HyperiumAddonBootstrap() throws HyperiumException {
        if (!modDirectory.mkdirs() && !modDirectory.exists()) {
            throw new HyperiumException("Unable to create addon directory!");
        }

        this.addons = Arrays
                .stream(modDirectory.listFiles())
                .filter(file -> file.getName().endsWith(".jar"))
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public void loadInternalAddon() throws Exception {
        try {
            JsonParser parser = new JsonParser();
            URL resource = Hyperium.class.getClassLoader().getResource("addon.json");
            String lines = IOUtils.toString(resource.openStream());
            JsonObject json = parser.parse(lines).getAsJsonObject();

            if (!json.has("version") && !json.has("name") && !json.has("main")) {
                throw new HyperiumException("Invalid addon (addon.json does not exist or invalid)");
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
     *
     * @param loader Addon loader
     */
    public void loadAddons(AddonLoaderStrategy loader) {
        Stopwatch benchmark = Stopwatch.createStarted();
        Hyperium.LOGGER.info("Starting to load addons...");
        for (File addon : this.addons) {
            try {
                loader.load(addon);
                Hyperium.LOGGER.info("Loaded {}", addon.getName());
            } catch (Exception e) {
                Hyperium.LOGGER.error("Could not load {}!", addon.getName());
                e.printStackTrace();
            }
        }
        Hyperium.LOGGER.debug("Finished loading all addons in {}.", benchmark);

    }

}
