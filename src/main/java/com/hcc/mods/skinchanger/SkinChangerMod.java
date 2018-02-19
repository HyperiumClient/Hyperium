package com.hcc.mods.skinchanger;

import com.hcc.HCC;
import com.hcc.event.EventBus;
import com.hcc.mods.IBaseMod;
import com.hcc.mods.skinchanger.commands.CommandSkinChanger;
import com.hcc.mods.skinchanger.config.SkinChangerConfig;
import com.hcc.mods.skinchanger.events.SkinChangerEvents;
import com.hcc.utils.ChatColor;

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
        this.config = new SkinChangerConfig(this, HCC.folder);
        this.config.load();
        
        EventBus.INSTANCE.register(new SkinChangerEvents(this));
        HCC.INSTANCE.getHandlers().getHCCCommandHandler().registerCommand(new CommandSkinChanger(this));
        
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