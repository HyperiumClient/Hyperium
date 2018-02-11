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

import com.hcc.config.DefaultConfig;
import com.hcc.event.InitializationEvent;
import com.hcc.event.InvokeEvent;
import com.hcc.event.RenderEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.File;

/**
 * Hypixel Community Client
 */
public class HCC {

    public static final HCC INSTANCE = new HCC();
    public static final String VERSION = "1.0 DEV";

    public static final DefaultConfig config = new DefaultConfig(new File("/hcc/config.json"));

    /**
     * Instance of the global mod logger
     */
    public final static Logger logger = LogManager.getLogger(Metadata.getModid());

    @InvokeEvent
    public void init(InitializationEvent event) {
        logger.info("HCC Started!");
        Display.setTitle("HCC " + VERSION);
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    @InvokeEvent
    public void render(RenderEvent event) {

    }

    public void shutdown() {
        config.save();
    }

}
