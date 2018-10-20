package cc.hyperium.mixinsimp.renderer;


public class CachedString {
    private String text;
    private int listId;
    private float width;
    private float height;
    private float lastRed;
    private float lastBlue;
    private float lastGreen;
    private float lastAlpha;

    public CachedString(String text, int listId, float width, float height) {
        this.text = text;
        this.listId = listId;
        this.width = width;
        this.height = height;
    }

    public float getLastAlpha() {
        return lastAlpha;
    }

    public void setLastAlpha(float lastAlpha) {
        this.lastAlpha = lastAlpha;
    }

    public float getLastRed() {
        return lastRed;
    }

    public void setLastRed(float lastRed) {
        this.lastRed = lastRed;
    }

    public float getLastBlue() {
        return lastBlue;
    }

    public void setLastBlue(float lastBlue) {
        this.lastBlue = lastBlue;
    }

    public float getLastGreen() {
        return lastGreen;
    }

    public void setLastGreen(float lastGreen) {
        this.lastGreen = lastGreen;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float v) {
        this.width = v;
    }

    public float getHeight() {
        return height;
    }

    public String getText() {
        return text;
    }

    public int getListId() {
        return listId;
    }
}
