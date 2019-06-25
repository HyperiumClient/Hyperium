package cc.hyperium.mods.levelhead.config;

public class MasterConfig {

    private boolean enabled = true;
    private int renderDistance = 64;
    private int purgeSize = 500;

    private double fontSize = 1.0;
    private double offset = 0.0;

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

    public int getPurgeSize() {
        return purgeSize;
    }

    public void setPurgeSize(int purgeSize) {
        this.purgeSize = purgeSize;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }
}
