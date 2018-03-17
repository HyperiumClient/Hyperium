package cc.hyperium.handlers.handlers;

import cc.hyperium.config.ConfigOpt;

/**
 * @author Sk1er
 */
public class OtherConfigOptions {
    @ConfigOpt
    public double headScaleFactor = 2.0;
    @ConfigOpt
    public boolean hideNameTags = false;
    @ConfigOpt
    public int renderNameDistance = 64;
    @ConfigOpt
    public boolean shadeNameTags = true;

}
