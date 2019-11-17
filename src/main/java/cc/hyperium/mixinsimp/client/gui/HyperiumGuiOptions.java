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

import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.keybinds.GuiKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.resources.I18n;

import java.util.List;

public class HyperiumGuiOptions {

    private GuiOptions parent;

    public HyperiumGuiOptions(GuiOptions parent) {
        this.parent = parent;
    }

    public void initGui(List<GuiButton> buttonList) {
        buttonList.forEach(b -> {
            if (b.id == 200) {
                b.yPosition = parent.height - 30;
            }
        });

        buttonList.add(new GuiButton(114514, parent.width / 2 - 155, parent.height / 6 + 18, 150, 20,
            I18n.format("button.ingame.hyperiumsettings")));

        buttonList.add(new GuiButton(62727568, parent.width / 2 + 5, parent.height / 6 + 18, 150, 20,
            "Hyperium Keybinds"));
    }

    public void actionPerformed(GuiButton button) {
        if (button.id == 114514) HyperiumMainGui.INSTANCE.show();
        if (button.id == 62727568) Minecraft.getMinecraft().displayGuiScreen(new GuiKeybinds());
    }
}
