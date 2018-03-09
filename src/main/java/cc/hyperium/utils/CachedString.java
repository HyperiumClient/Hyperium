package cc.hyperium.utils;

import net.minecraft.client.renderer.texture.DynamicTexture;

import java.awt.image.BufferedImage;

/**
 * Created by mitchellkatz on 3/9/18. Designed for production use on Sk1er.club
 */
public class CachedString {
    private DynamicTexture texture;
    private int width;
    private int height;
    private int returnThing;

    private BufferedImage image;

    public CachedString(BufferedImage texture) {
        this.texture = new DynamicTexture(texture);
        this.image = texture;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public DynamicTexture getTexture() {
        return texture;
    }

    public int getReturnThing() {
        return returnThing;
    }

    public void setReturnThing(int returnThing) {
        this.returnThing = returnThing;
    }
}
