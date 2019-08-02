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

package cc.hyperium.mods.levelhead.display;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.utils.ChatColor;

public class DisplayConfig {

    @ConfigOpt private boolean enabled = true;
    @ConfigOpt private boolean showSelf = true;

    @ConfigOpt private String type = "LEVEL";

    @ConfigOpt private boolean headerChroma;
    @ConfigOpt private boolean headerRgb;
    @ConfigOpt private int headerRed = 255;
    @ConfigOpt private int headerGreen = 255;
    @ConfigOpt private int headerBlue = 255;
    @ConfigOpt private String headerColor = ChatColor.AQUA.toString();
    @ConfigOpt private String customHeader = "Level";

    @ConfigOpt private boolean footerChroma;
    @ConfigOpt private boolean footerRgb;
    @ConfigOpt private int footerRed = 255;
    @ConfigOpt private int footerGreen = 255;
    @ConfigOpt private int footerBlue = 255;
    @ConfigOpt private String footerColor = ChatColor.YELLOW.toString();

    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public boolean isShowSelf() {
        return showSelf;
    }
    public void setShowSelf(boolean showSelf) {
        this.showSelf = showSelf;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
    double getHeaderAlpha() {
        return 1.0;
    }
    public String getHeaderColor() {
        return headerColor;
    }
    public void setHeaderColor(String headerColor) {
        this.headerColor = headerColor;
    }
    public String getCustomHeader() {
        return customHeader;
    }
    public void setCustomHeader(String customHeader) {
        this.customHeader = customHeader;
    }
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
    double getFooterAlpha() {
        return 1.0;
    }
    public String getFooterColor() {
        return footerColor;
    }
    public void setFooterColor(String footerColor) {
        this.footerColor = footerColor;
    }
}
