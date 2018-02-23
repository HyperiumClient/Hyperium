/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.mods.skinchanger;

import cc.hyperium.mods.skinchanger.commands.CommandSkinChanger;
import cc.hyperium.mods.skinchanger.config.SkinChangerConfig;
import cc.hyperium.mods.skinchanger.events.SkinChangerEvents;
import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.IBaseMod;
import cc.hyperium.utils.ChatColor;

/**
 * Also a lightweight version of SkinChanger
 *
 * @author boomboompower
 */
public class SkinChangerMod extends IBaseMod {
    
    /**
     * The mods metadata
     */
    private final Metadata metaData;
    
    /**
     * SkinChangers config
     */
    private SkinChangerConfig config;
    
    /**
     * The default SkinChanger constructor, sets the metadata and such
     */
    public SkinChangerMod() {
        Metadata data = new Metadata(this, "SkinChanger", "2.0", "boomboompower");
        
        data.setDisplayName(ChatColor.AQUA + "SkinChanger");
        
        this.metaData = data;
    }
    
    public IBaseMod init() {
        this.config = new SkinChangerConfig(this, Hyperium.folder);
        this.config.load();
        
        EventBus.INSTANCE.register(new SkinChangerEvents(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandSkinChanger(this));
        
        return this;
    }
    
    @Override
    public Metadata getModMetadata() {
        return this.metaData;
    }
    
    /**
     * Getter for SkinChanger's configuration gui
     *
     * @return the configuration
     */
    public SkinChangerConfig getConfig() {
        return this.config;
    }
}