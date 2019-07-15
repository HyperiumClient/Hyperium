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

package cc.hyperium.mixinsimp.client.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;

import java.util.List;

public class HyperiumGuiScreenResourcePacks {

    private GuiScreenResourcePacks parent;

    public HyperiumGuiScreenResourcePacks(GuiScreenResourcePacks parent) {
        this.parent = parent;
    }

    public void initGui(List<GuiButton> buttonList) {
        buttonList.forEach(b -> {
            b.setWidth(200);
            if (b.id == 2) {
                b.xPosition = parent.width / 2 - 204;
            }
        });
    }

    public void drawScreen(GuiResourcePackAvailable availableResourcePacksList, GuiResourcePackSelected selectedResourcePacksList, int mouseX, int mouseY, float partialTicks, FontRenderer fontRendererObj, int width) {
        parent.drawBackground(0);
        availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        parent.drawCenteredString(fontRendererObj, I18n.format("resourcePack.title"), width / 2,
            16, 16777215);
    }
}
