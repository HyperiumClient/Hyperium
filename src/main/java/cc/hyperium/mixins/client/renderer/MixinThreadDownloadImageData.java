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

package cc.hyperium.mixins.client.renderer;

import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

@Mixin(ThreadDownloadImageData.class)
public abstract class MixinThreadDownloadImageData extends SimpleTexture {

    @Shadow @Final private String imageUrl;
    @Shadow @Final private File cacheFile;
    @Shadow @Final private IImageBuffer imageBuffer;
    @Shadow public abstract void setBufferedImage(BufferedImage bufferedImageIn);

    public MixinThreadDownloadImageData(ResourceLocation textureResourceLocation) {
        super(textureResourceLocation);
    }

    /**
     * @author Sk1er
     * @reason Create thread pool for ThreadDownloadImageData to stop excessive concurrency and not create thousands
     */
    @Overwrite
    protected void loadTextureFromServer() {
        Multithreading.runAsync(() -> {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) (new URL(imageUrl)).openConnection(Minecraft.getMinecraft().getProxy());
                connection.setRequestProperty("User-Agent", "Hyperium Client");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                if (connection.getResponseCode() / 100 == 2) {
                    BufferedImage image;

                    if (cacheFile != null) {
                        FileUtils.copyInputStreamToFile(connection.getInputStream(), cacheFile);
                        image = ImageIO.read(cacheFile);
                    } else {
                        image = TextureUtil.readBufferedImage(connection.getInputStream());
                    }

                    if (imageBuffer != null) image = imageBuffer.parseUserSkin(image);
                    setBufferedImage(image);
                }
            } catch (Exception ignored) {
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }
}
