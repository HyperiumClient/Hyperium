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

package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/*
 * Created by Cubxity on 01/06/2018
 */
public enum Icons {

    SETTINGS(new ResourceLocation("hyperium","textures/material/settings.png")),
    EXTENSION(new ResourceLocation("hyperium","textures/material/extension.png")),
    DOWNLOAD(new ResourceLocation("hyperium","textures/material/download.png")),
    EXIT(new ResourceLocation("hyperium","textures/material/exit.png")),
    CLOSE(new ResourceLocation("hyperium","textures/material/close.png")),
    ARROW_DOWN_ALT(new ResourceLocation("hyperium","textures/material/arrow_down_alt.png")),
    ARROW_UP_ALT(new ResourceLocation("hyperium","textures/material/arrow_up_alt.png")),
    ARROW_LEFT(new ResourceLocation("hyperium","textures/material/arrow_left.png")),
    ARROW_RIGHT(new ResourceLocation("hyperium","textures/material/arrow_right.png")),
    COSMETICS(new ResourceLocation("hyperium", "textures/material/shopping_bag.png")),
    MENU_BACKGROUND(new ResourceLocation("hyperium", "textures/material/gallery.png"));

    private ResourceLocation res;

    Icons(ResourceLocation res) {
        this.res = res;
    }

    public void bind() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
    }

    public ResourceLocation getResource() {
        return res;
    }

}
