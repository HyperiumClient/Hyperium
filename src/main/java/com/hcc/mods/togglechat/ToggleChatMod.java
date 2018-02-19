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

package com.hcc.mods.togglechat;

import com.hcc.HCC;
import com.hcc.event.EventBus;
import com.hcc.event.InvokeEvent;
import com.hcc.event.TickEvent;
import com.hcc.mods.togglechat.commands.CommandToggleChat;
import com.hcc.mods.togglechat.config.ToggleChatConfig;
import com.hcc.mods.togglechat.gui.MainGui;
import com.hcc.mods.togglechat.toggles.ToggleBaseHandler;
import net.minecraft.client.Minecraft;

/**
 * Basically just a lightweight version of ToggleChat
 *
 * @author boomboompower
 */
public final class ToggleChatMod {

    /**
     * A basic CONFIG loader
     */
    private ToggleChatConfig configLoader;

    /**
     * A different implementation to the normal ToggleChat, just manages all toggles
     */
    private ToggleBaseHandler toggleHandler;

    /**
     * A flag for opening our gui
     */
    private boolean opening;

    public ToggleChatMod init() {
        this.configLoader = new ToggleChatConfig(this, HCC.folder);

        this.toggleHandler = new ToggleBaseHandler();
        this.toggleHandler.remake();

        EventBus.INSTANCE.register(new ToggleEvents(this));
        EventBus.INSTANCE.register(this);

        HCC.INSTANCE.getHandlers().getHCCCommandHandler().registerCommand(new CommandToggleChat(this));

        this.configLoader.loadToggles();

        return this;
    }

    public ToggleChatConfig getConfigLoader() {
        return this.configLoader;
    }

    public ToggleBaseHandler getToggleHandler() {
        return this.toggleHandler;
    }

    @InvokeEvent
    public void onTick(TickEvent event) {
        if (this.opening) {
            // Sets opening to false and opens the main screen
            this.opening = false;
            Minecraft.getMinecraft().displayGuiScreen(new MainGui(this, 1));
        }
    }

    /**
     * Tells the mod it should open the gui
     */
    public void openGui() {
        this.opening = true;
    }
}
