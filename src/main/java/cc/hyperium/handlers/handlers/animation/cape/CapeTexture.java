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
        this.dynamicTextureData = new int[0];
    }

    public int[] getTextureData() {
        return this.dynamicTextureData;
    }
}
