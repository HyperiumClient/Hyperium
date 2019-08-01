/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.nickhider.config;

import org.apache.commons.lang3.RandomStringUtils;

public class NickHiderConfig {

    private String pseudoKey = RandomStringUtils.random(6, true, true);
    private String prefix = "Player-";
    private String suffix = "";
    private boolean masterEnabled;
    private boolean hideNames;
    private boolean hideOtherNames;
    private boolean hideOtherSkins;
    private boolean hideSkins;
    private boolean usePlayerSkinForAll;
    private boolean useRealSkinForSelf;

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
