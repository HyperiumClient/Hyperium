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

    SETTINGS(new ResourceLocation("textures/material/settings.png")),
    EXTENSION(new ResourceLocation("textures/material/extension.png")),
    DOWNLOAD(new ResourceLocation("textures/material/download.png")),
    EXIT(new ResourceLocation("textures/material/exit.png")),
    CLOSE(new ResourceLocation("textures/material/close.png")),
    ARROW_DOWN_ALT(new ResourceLocation("textures/material/arrow_down_alt.png")),
    ARROW_UP_ALT(new ResourceLocation("textures/material/arrow_up_alt.png")),
    ARROW_LEFT(new ResourceLocation("textures/material/arrow_left.png")),
    ARROW_RIGHT(new ResourceLocation("textures/material/arrow_right.png"));

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
