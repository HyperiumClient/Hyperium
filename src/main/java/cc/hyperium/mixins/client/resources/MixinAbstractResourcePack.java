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

package cc.hyperium.mixins.client.resources;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Mixin(AbstractResourcePack.class)
public abstract class MixinAbstractResourcePack implements IResourcePack {

    /**
     * @author prplz
     */


    private static final int IconSize = 64;

    @Shadow
    public abstract InputStream getInputStream(ResourceLocation location) throws IOException;

    @Shadow
    protected abstract InputStream getInputStreamByName(String name) throws IOException;

    /**
     * @author prplz
     */
    @Overwrite
    public BufferedImage getPackImage() throws IOException {
        BufferedImage originalIcon = TextureUtil.readBufferedImage(this.getInputStreamByName("pack.png"));
        if (originalIcon == null) {
            return null;
        }
        BufferedImage resizedIcon = new BufferedImage(IconSize, IconSize, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = resizedIcon.getGraphics();
        graphics.drawImage(originalIcon, 0, 0, IconSize, IconSize, null);
        graphics.dispose();
        return resizedIcon;
    }
}
