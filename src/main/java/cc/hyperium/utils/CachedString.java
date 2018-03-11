/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
