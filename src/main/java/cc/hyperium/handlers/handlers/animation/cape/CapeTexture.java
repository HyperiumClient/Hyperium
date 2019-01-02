package cc.hyperium.handlers.handlers.animation.cape;


import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

import java.awt.image.BufferedImage;
import java.io.IOException;


//Basicly just a dynamic texture but allows for cleanup
public class CapeTexture extends AbstractTexture {
    /**
     * width of this icon in pixels
     */
    private final int width;
    /**
     * height of this icon in pixels
     */
    private final int height;
    private int[] dynamicTextureData;

    public CapeTexture(BufferedImage bufferedImage) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight());
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.dynamicTextureData, 0, bufferedImage.getWidth());
        this.updateDynamicTexture();
    }

    public CapeTexture(int textureWidth, int textureHeight) {
        this.width = textureWidth;
        this.height = textureHeight;
        this.dynamicTextureData = new int[textureWidth * textureHeight];
        TextureUtil.allocateTexture(this.getGlTextureId(), textureWidth, textureHeight);
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
    }

    public void updateDynamicTexture() {
        TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
    }

    public void clearTextureData() {
        this
            .dynamicTextureData = new int[0];
    }

    public int[] getTextureData() {
        return this.dynamicTextureData;
    }
}
