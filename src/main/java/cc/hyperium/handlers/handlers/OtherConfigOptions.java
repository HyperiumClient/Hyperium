package cc.hyperium.handlers.handlers;

import cc.hyperium.config.ConfigOpt;

/**
 * @author Sk1er
 */
public class OtherConfigOptions {
    @ConfigOpt
    public final double headScaleFactor = 2.0;
    @ConfigOpt
    public final int renderNameDistance = 64;
    @ConfigOpt
    public final boolean shadeNameTags = true;
    @ConfigOpt
    public final int stringCacheSize = 1000;
    @ConfigOpt
    public boolean hideNameTags = false;
    @ConfigOpt
    public boolean showCosmeticsEveryWhere = true;
    @ConfigOpt
    public boolean alternateFontRenderer = false;

    @ConfigOpt
    public boolean enableDeadmau5Ears = true;

    @ConfigOpt
    public boolean friendsFirstIntag = true;
    @ConfigOpt
    public boolean pingOnDm = true;

    @ConfigOpt
    public boolean showOnlinePlayers = true;
}
