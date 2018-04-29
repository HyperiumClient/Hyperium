package cc.hyperium.handlers.handlers;

import cc.hyperium.config.ConfigOpt;

/**
 * @author Sk1er
 */
public class OtherConfigOptions {
    @ConfigOpt
    public final double headScaleFactor = 2.0;
    @ConfigOpt
    public boolean hideNameTags = false;
    @ConfigOpt
    public final int renderNameDistance = 64;
    @ConfigOpt
    public final boolean shadeNameTags = true;

    @ConfigOpt
    public boolean showCosmeticsEveryWhere = true;

    @ConfigOpt
    public final int stringCacheSize = 1000;

    @ConfigOpt
    public boolean alternateFontRenderer = false;

    @ConfigOpt
    public boolean enableDeadmau5Ears = true;
}
