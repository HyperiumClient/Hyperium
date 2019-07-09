package cc.hyperium.mods.blockoverlay;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;

public class BlockOverlay extends AbstractMod {
    public final static Minecraft mc = Minecraft.getMinecraft();

    private final Metadata meta;
    private BlockOverlaySettings settings;

    public BlockOverlay() {
        this.meta = new Metadata(this, "BlockOverlay", "1.0", "aycy & powns");
        this.meta.setDisplayName(ChatColor.RED + "BlockOverlay");
    }

    @Override
    public AbstractMod init() {
        this.settings = new BlockOverlaySettings(Hyperium.folder);
        this.settings.load();
        EventBus.INSTANCE.register(new BlockOverlayRender(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new BlockOverlayCommand(this));
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return this.meta;
    }

    public BlockOverlaySettings getSettings() {
        return this.settings;
    }
}
