package cc.hyperium.mods.nickhider.config;

import org.apache.commons.lang3.RandomStringUtils;

public class NickHiderConfig {

    private String pseudoKey = RandomStringUtils.random(6, true, true);
    private String prefix = "Player-";
    private String suffix = "";
    private boolean masterEnabled = false;
    private boolean hideNames = false;
    private boolean hideOtherNames = false;
    private boolean hideOtherSkins = false;
    private boolean hideSkins = false;
    private boolean usePlayerSkinForAll = false;
    private boolean useRealSkinForSelf = false;

    public String getPseudoKey() {
        return pseudoKey;
    }
    public void setPseudoKey(String pseudoKey) {
        this.pseudoKey = pseudoKey;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public boolean isMasterEnabled() {
        return masterEnabled;
    }
    public void setMasterEnabled(boolean masterEnabled) {
        this.masterEnabled = masterEnabled;
    }
    public boolean isHideNames() {
        return hideNames;
    }
    public void setHideNames(boolean hideNames) {
        this.hideNames = hideNames;
    }
    public boolean isHideOtherNames() {
        return hideOtherNames;
    }
    public void setHideOtherNames(boolean hideOtherNames) {
        this.hideOtherNames = hideOtherNames;
    }
    public boolean isHideOtherSkins() {
        return hideOtherSkins;
    }
    public void setHideOtherSkins(boolean hideOtherSkins) {
        this.hideOtherSkins = hideOtherSkins;
    }
    public boolean isHideSkins() {
        return hideSkins;
    }
    public void setHideSkins(boolean hideSkins) {
        this.hideSkins = hideSkins;
    }
    public boolean isUsePlayerSkinForAll() {
        return usePlayerSkinForAll;
    }
    public void setUsePlayerSkinForAll(boolean usePlayerSkinForAll) {
        this.usePlayerSkinForAll = usePlayerSkinForAll;
    }
    public boolean isUseRealSkinForSelf() {
        return useRealSkinForSelf;
    }
    public void setUseRealSkinForSelf(boolean useRealSkinForSelf) {
        this.useRealSkinForSelf = useRealSkinForSelf;
    }
}
