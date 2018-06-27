/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.levelhead.config;


import cc.hyperium.config.ConfigOpt;
import cc.hyperium.utils.ChatColor;

/**
 * @author Sk1er
 */
public class LevelheadConfig {

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;enabled")
    private boolean enabled = true;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;showSelf")
    private boolean showSelf = true;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;renderDistance")
    private int renderDistance = 64;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;purgeSize")
    private int purgeSize = 500;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;headerChroma")
    private boolean headerChroma = false;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;headerRgb")
    private boolean headerRgb = false;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;headerColor")
    private String headerColor = ChatColor.AQUA.toString();

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;headerRed")
    private int headerRed = 255;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;headerGreen")
    private int headerGreen = 255;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;headerBlue")
    private int headerBlue = 250;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;headerAlpha")
    private double headerAlpha = 1.0;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;customHeader")
    private String customHeader = "Level";

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;footerChroma")
    private boolean footerChroma = false;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;footerRgb")
    private boolean footerRgb = false;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;footerColor")
    private String footerColor = ChatColor.YELLOW.toString();

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;footerRed")
    private int footerRed = 255;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;footerGreen")
    private int footerGreen = 255;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;footerBlue")
    private int footerBlue = 250;

    @ConfigOpt(alt = "club.sk1er.mods.levelhead.config.LevelheadConfig;footerAlpha")
    private double footerAlpha = 1.0;


    public boolean isFooterChroma() {
        return footerChroma;
    }

    public void setFooterChroma(boolean footerChroma) {
        this.footerChroma = footerChroma;
    }

    public boolean isFooterRgb() {
        return footerRgb;
    }

    public void setFooterRgb(boolean footerRgb) {
        this.footerRgb = footerRgb;
    }

    public String getFooterColor() {
        return footerColor;
    }

    public void setFooterColor(String footerColor) {
        this.footerColor = footerColor;
    }

    public int getFooterRed() {
        return footerRed;
    }

    public void setFooterRed(int footerRed) {
        this.footerRed = footerRed;
    }

    public int getFooterGreen() {
        return footerGreen;
    }

    public void setFooterGreen(int footerGreen) {
        this.footerGreen = footerGreen;
    }

    public int getFooterBlue() {
        return footerBlue;
    }

    public void setFooterBlue(int footerBlue) {
        this.footerBlue = footerBlue;
    }

    public double getFooterAlpha() {
        return footerAlpha;
    }

    public void setFooterAlpha(double footerAlpha) {
        this.footerAlpha = footerAlpha;
    }


    public boolean isHeaderChroma() {
        return headerChroma;
    }

    public void setHeaderChroma(boolean headerChroma) {
        this.headerChroma = headerChroma;
    }

    public boolean isHeaderRgb() {
        return headerRgb;
    }

    public void setHeaderRgb(boolean headerRgb) {
        this.headerRgb = headerRgb;
    }

    public String getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(String headerColor) {
        this.headerColor = headerColor;
    }

    public int getHeaderRed() {
        return headerRed;
    }

    public void setHeaderRed(int headerRed) {
        this.headerRed = headerRed;
    }

    public int getHeaderGreen() {
        return headerGreen;
    }

    public void setHeaderGreen(int headerGreen) {
        this.headerGreen = headerGreen;
    }

    public int getHeaderBlue() {
        return headerBlue;
    }

    public void setHeaderBlue(int headerBlue) {
        this.headerBlue = headerBlue;
    }

    public double getHeaderAlpha() {
        return headerAlpha;
    }

    public void setHeaderAlpha(double headerAlpha) {
        this.headerAlpha = headerAlpha;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getRenderDistance() {
        return renderDistance;
    }

    public void setRenderDistance(int renderDistance) {
        this.renderDistance = renderDistance;
    }

    public boolean isShowSelf() {
        return showSelf;
    }

    public void setShowSelf(boolean showSelf) {
        this.showSelf = showSelf;
    }


    public int getPurgeSize() {
        return purgeSize;
    }

    public void setPurgeSize(int purgeSize) {
        this.purgeSize = purgeSize;
    }

    public String getCustomHeader() {
        return customHeader;
    }

    public void setCustomHeader(String customHeader) {
        this.customHeader = customHeader;
    }
}
