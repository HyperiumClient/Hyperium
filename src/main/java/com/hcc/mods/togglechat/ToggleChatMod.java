/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018 HCC Dev Team
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
import com.hcc.event.*;
import com.hcc.mods.togglechat.config.ConfigLoader;
import com.hcc.mods.togglechat.gui.MainGui;
import com.hcc.mods.togglechat.toggles.ToggleBaseHandler;

import net.minecraft.client.Minecraft;

/**
 * Basically just a lightweight version of ToggleChat
 *
 * @author boomboompower
 */
public class ToggleChatMod {

    /** ToggleChat lite! */
    public static final String MODID = "togglechat_lite";
    public static final String VERSION = "1.0";

    /** A basic config loader */
    private ConfigLoader configLoader;

    /** A different implementation to the normal ToggleChat, just manages all toggles */
    private ToggleBaseHandler toggleHandler;

    private boolean opening;

    public ToggleChatMod init() {
        this.configLoader = new ConfigLoader(this, HCC.folder);

        this.toggleHandler = new ToggleBaseHandler();
        this.toggleHandler.remake();

        EventBus.INSTANCE.register(new ToggleEvents(this));
        EventBus.INSTANCE.register(this);

        this.configLoader.loadToggles();

        return this;
    }

    public ConfigLoader getConfigLoader() {
        return this.configLoader;
    }

    public ToggleBaseHandler getToggleHandler() {
        return this.toggleHandler;
    }

    @InvokeEvent
    public void onTick(TickEvent event) {
        if (this.opening) {
            this.opening = false;
            Minecraft.getMinecraft().displayGuiScreen(new MainGui(this, 1));
        }
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onChat(SendChatMessageEvent event) { // TODO boomboompower command implementation
        if (event.getMessage().startsWith("/") && event.getMessage().equalsIgnoreCase("/tc")) {
            this.opening = true;
            event.setCancelled(true);
        }
    }
}
