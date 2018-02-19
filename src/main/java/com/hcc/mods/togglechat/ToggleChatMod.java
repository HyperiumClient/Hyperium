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
import com.hcc.mods.IBaseMod;
import com.hcc.mods.togglechat.commands.CommandToggleChat;
import com.hcc.mods.togglechat.config.ToggleChatConfig;
import com.hcc.mods.togglechat.toggles.ToggleBaseHandler;
import com.hcc.utils.ChatColor;

/**
 * Basically just a lightweight version of ToggleChat
 *
 * @author boomboompower
 */
public final class ToggleChatMod extends IBaseMod {
    
    /**
     * The metadata of ToggleChat
     */
    private final Metadata meta;
    
    /**
     * A basic CONFIG loader
     */
    private ToggleChatConfig configLoader;

    /**
     * A different implementation to the normal ToggleChat, just manages all toggles
     */
    private ToggleBaseHandler toggleHandler;
    
    public ToggleChatMod() {
        Metadata metadata = new Metadata(this, "ToggleChatLite", "1.0", "boomboompower");
        
        metadata.setDisplayName(ChatColor.AQUA + "ToggleChatLite");
        
        this.meta = metadata;
    }
    
    public IBaseMod init() {
        this.configLoader = new ToggleChatConfig(this, HCC.folder);
    
        this.toggleHandler = new ToggleBaseHandler();
        this.toggleHandler.remake();
    
        EventBus.INSTANCE.register(new ToggleChatEvents(this));
    
        HCC.INSTANCE.getHandlers().getHCCCommandHandler().registerCommand(new CommandToggleChat(this));
    
        this.configLoader.loadToggles();
        
        return this;
    }
    
    @Override
    public Metadata getModMetadata() {
        return this.meta;
    }
    
    /**
     * Getter for ToggleChat's configuration
     *
     * @return the configuration
     */
    public ToggleChatConfig getConfigLoader() {
        return this.configLoader;
    }
    
    /**
     * Getter for ToggleChat's ToggleHandler
     *
     * @return the handlers instance
     */
    public ToggleBaseHandler getToggleHandler() {
        return this.toggleHandler;
    }
}
