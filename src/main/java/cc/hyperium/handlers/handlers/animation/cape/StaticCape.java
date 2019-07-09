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

import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class StaticCape implements ICape {
    private ResourceLocation location;

    public StaticCape(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public ResourceLocation get() {
        return location;
    }

    @Override
    public void delete(TextureManager manager) {
        ITextureObject texture = manager.getTexture(location);
        if (texture instanceof ThreadDownloadImageData) {
            //Unlink the buffered image so garbage collector can do its magic
            ((ThreadDownloadImageData) texture).setBufferedImage(null);
        }
        manager.deleteTexture(location);
    }
}
