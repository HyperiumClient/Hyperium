package cc.hyperium.mods.itemphysic;

import cc.hyperium.config.ConfigOpt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemDummyContainer {

    public static final Logger logger = LogManager.getLogger("ItemPhysics");

    @ConfigOpt
    public static float rotateSpeed = 1.0F;

}
