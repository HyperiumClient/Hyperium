package com.hcc;

import com.hcc.event.InitializationEvent;
import com.hcc.event.InvokeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hypixel Community Client
 */
public class HCC {

    /**
     * Instance of the global mod logger
     */
    public final static Logger logger = LogManager.getLogger(Metadata.getModid());

    @InvokeEvent
    public static void init(InitializationEvent event) {
        logger.info("HCC Started!");
    }


}
