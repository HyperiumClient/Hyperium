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