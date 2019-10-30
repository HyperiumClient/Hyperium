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

import cc.hyperium.mixins.client.resources.IMixinResourcePackListEntry;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;

import java.util.Arrays;
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

    public void drawScreen(GuiResourcePackAvailable availableResourcePacksList, GuiResourcePackSelected selectedResourcePacksList,
                           int mouseX, int mouseY, float partialTicks, FontRenderer fontRendererObj, int width) {
        parent.drawBackground(0);
        availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        parent.drawCenteredString(fontRendererObj, I18n.format("resourcePack.title"), width / 2, 16, 16777215);
    }

    public GuiResourcePackAvailable updateList(GuiTextField searchField, GuiResourcePackAvailable availablePacksClone,
                                               List<ResourcePackListEntry> availableResourcePacks, Minecraft mc, int width, int height) {
        GuiResourcePackAvailable availableResourcePacksList;
        if (searchField == null || searchField.getText().isEmpty()) {
            availableResourcePacksList = new GuiResourcePackAvailable(mc, 200, height, availableResourcePacks);
            availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
            availablePacksClone.registerScrollButtons(7, 8);
        } else {
            availableResourcePacksList = new GuiResourcePackAvailable(mc, 200, height, Arrays.asList(availablePacksClone.getList().stream()
                .filter(resourcePackListEntry -> {
                try {
                    String name = ChatColor.stripColor(((IMixinResourcePackListEntry) resourcePackListEntry).callFunc_148312_b().
                        replaceAll("[^A-Za-z0-9 ]", "").trim().toLowerCase());
                    String text = searchField.getText().toLowerCase();

                    if (name.endsWith("zip")) {
                        name = name.subSequence(0, name.length() - 3).toString();
                    }

                    for (String s : text.split(" ")) {
                        if (!name.contains(s.toLowerCase())) {
                            return false;
                        }
                    }

                    return name.startsWith(text) || name.contains(text) || name.equalsIgnoreCase(text);
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }).toArray(ResourcePackListEntry[]::new)));

            availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
            availableResourcePacksList.registerScrollButtons(7, 8);
        }

        return availableResourcePacksList;
    }
}
