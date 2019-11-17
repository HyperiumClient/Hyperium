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

import cc.hyperium.gui.keybinds.GuiKeybinds;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class HyperiumGuiControls extends GuiScreen {

    public void initGui(List<GuiButton> buttonList) {
        buttonList.add(new GuiButton(10, width / 2 + buttonList.get(1).xPosition, height / 2 + buttonList.get(2).yPosition + 24,
            150, 20, "Hyperium Keybinds"));
    }

    public void actionPerformed(GuiButton button) {
        if (button.id == 10) Minecraft.getMinecraft().displayGuiScreen(new GuiKeybinds());
    }
}
