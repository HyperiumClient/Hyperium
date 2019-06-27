package cc.hyperium.mods.levelhead.display;

import cc.hyperium.utils.ChatColor;

public class DisplayConfig {

    private boolean enabled = true;
    private boolean showSelf = true;

    private String type = "LEVEL";

    private boolean headerChroma = false;
    private boolean headerRgb = false;
    private int headerRed = 255;
    private int headerGreen = 255;
    private int headerBlue = 255;
    private String headerColor = ChatColor.AQUA.toString();
    private String customHeader = "Level";

    private boolean footerChroma = false;
    private boolean footerRgb = false;
    private int footerRed = 255;
    private int footerGreen = 255;
    private int footerBlue = 255;
    private String footerColor = ChatColor.YELLOW.toString();

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
