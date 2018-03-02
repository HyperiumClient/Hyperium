package cc.hyperium.mods.utilities;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.utils.ChatColor;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

public class UtilitiesMod extends AbstractMod {
    
    private final Metadata metadata;
    
    private final KeyBinding binding = new KeyBinding("Zoom", Keyboard.KEY_C, "Utilities");
    
    public UtilitiesMod() {
        Metadata metadata = new Metadata(this, "Utilities", "1.0", "boomboompower");
    
        metadata.setDisplayName(ChatColor.AQUA + "Utilities");
    
        this.metadata = metadata;
    }
    
    @Override
    public AbstractMod init() {
    
        Hyperium.INSTANCE.getHandlers().getKeybindHandler().registerKeyBinding(this.binding);
        
        return this;
    }
    
    @Override
    public Metadata getModMetadata() {
        return this.metadata;
    }
    
    public KeyBinding getBinding() {
        return this.binding;
    }
}
