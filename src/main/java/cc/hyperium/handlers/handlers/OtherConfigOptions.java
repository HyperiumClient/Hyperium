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

    @ConfigOpt
    public boolean showFlipEverywhere = true;

    @ConfigOpt
    public int stringCacheSize = 1000;

    @ConfigOpt
    public boolean alternateFontRenderer = false;

    @ConfigOpt
    public boolean enableDeadmau5Ears = true;
}
