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

package com.hcc;

import com.hcc.addons.HCCAddonBootstrap;
import com.hcc.addons.loader.DefaultAddonLoader;
import com.hcc.config.DefaultConfig;
import com.hcc.event.InitializationEvent;
import com.hcc.event.InvokeEvent;
import com.hcc.event.RenderEvent;
import com.hcc.exceptions.HCCException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.File;

/**
 * Hypixel Community Client
 */
public class HCC {

    public static final HCC INSTANCE = new HCC();

    /**
     * Instance of default config
     */
    public static final DefaultConfig config = new DefaultConfig(new File("/hcc/config.json"));

    /**
     * Instance of the global mod logger
     */
    public final static Logger logger = LogManager.getLogger(Metadata.getModid());

    /**
     * Instance of default addons loader
     */
    public static final DefaultAddonLoader addonLoader = new DefaultAddonLoader();

    private static HCCAddonBootstrap addonBootstrap;

    static {
        try {
            addonBootstrap = new HCCAddonBootstrap();
        } catch (HCCException e) {
            e.printStackTrace();
            logger.error("failed to initialize addonBootstrap");
        }
    }



    @InvokeEvent
    public void init(InitializationEvent event) {
        logger.info("HCC Started!");
        Display.setTitle("HCC " + Metadata.getVersion());
        try {
            File addons = new File("/hcc/addons");
            addons.mkdir();
            addonBootstrap.loadAddons(addonLoader);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to load addon(s) from addons folder");
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    @InvokeEvent
    public void render(RenderEvent event) {

    }

    private void shutdown() {
        config.save();
        logger.info("Shutting down HCC..");
    }

}
