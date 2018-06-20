package cc.hyperium.mixinsimp.renderer;


public class CachedString {
    private String text;
    private int listId;
    private float width;
    private float height;

    public CachedString(String text, int listId, float width, float height) {
        this.text = text;
        this.listId = listId;
        this.width = width;
        this.height = height;
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
