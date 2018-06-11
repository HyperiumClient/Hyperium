package cc.hyperium.handlers.handlers;

import cc.hyperium.config.ConfigOpt;

/**
 * @author Sk1er
 */
public class OtherConfigOptions {
    @ConfigOpt
    public double headScaleFactor = 2.0;
    @ConfigOpt
    public int renderNameDistance = 64;
    @ConfigOpt
    public boolean shadeNameTags = true;
    @ConfigOpt
    public int stringCacheSize = 1000;
    @ConfigOpt
    public boolean hideNameTags = false;
    @ConfigOpt
    public boolean showCosmeticsEveryWhere = true;
    @ConfigOpt
    public boolean alternateFontRenderer = false;



    @ConfigOpt
    public boolean friendsFirstIntag = true;
    @ConfigOpt
    public boolean pingOnDm = true;

    @ConfigOpt
    public boolean showOnlinePlayers = true;
    public boolean isCancelBox;

    @ConfigOpt
    public boolean turnPeopleIntoBlock = false;

    @ConfigOpt
    public boolean limitCPS = false;

    @ConfigOpt
    public int maxCps = 2;

    @ConfigOpt
    public boolean showNotificationCenter = true;
    @ConfigOpt
    public boolean showConfirmationPopup = true;

    @ConfigOpt
    public boolean savePreviusChatMessages = false;
}
