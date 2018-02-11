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

    private File modDirectory = new File("./hcc/addons/");

    private ArrayList<File> addons;

    public HCCAddonBootstrap() throws HCCException {
        if (!modDirectory.mkdirs()) {
            throw new HCCException("Unable to create addon directory!");
        }

        this.addons = Arrays
                .stream(modDirectory.listFiles())
                .filter(file -> file.getName().endsWith(".jar"))
                .collect(Collectors.toCollection(ArrayList::new));

    }

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
