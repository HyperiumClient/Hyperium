package com.hcc.addons;

import com.google.common.base.Stopwatch;
import com.hcc.HCC;
import com.hcc.addons.loader.AddonLoaderStrategy;
import com.hcc.exceptions.HCCException;

import java.io.File;
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
        if (!modDirectory.mkdirs()) {
            throw new HCCException("Unable to create addon directory!");
        }

        this.addons = Arrays
                .stream(modDirectory.listFiles())
                .filter(file -> file.getName().endsWith(".jar"))
                .collect(Collectors.toCollection(ArrayList::new));

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
