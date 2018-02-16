/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.mods.chromahud.api;

import net.minecraft.client.gui.GuiButton;

import java.util.function.BiConsumer;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public class ButtonConfig {
    /*
        Called when the button is pressed. GuiButton is the gui button instance provided. DisplayItem is the object of that display item
        Load is called on load to initalize to right state
     */
    private BiConsumer<GuiButton, DisplayItem> action;
    private GuiButton button;
    private BiConsumer<GuiButton, DisplayItem> load;

    public ButtonConfig(BiConsumer<GuiButton, DisplayItem> action, GuiButton button, BiConsumer<GuiButton, DisplayItem> load) {
        this.action = action;
        this.button = button;
        this.load = load;
    }

    public BiConsumer<GuiButton, DisplayItem> getAction() {
        return action;
    }

    public GuiButton getButton() {
        return button;
    }

    public BiConsumer<GuiButton, DisplayItem> getLoad() {
        return load;
    }
}
