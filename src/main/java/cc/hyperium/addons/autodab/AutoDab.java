package cc.hyperium.addons.autodab;

import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.event.EventBus;
import cc.hyperium.utils.ChatColor;

public class AutoDab extends AbstractAddon {
    private boolean currentlyDabbing;
    static AutoDab INSTANCE;

    @Override
    public AbstractAddon init() {
        AutoDab.INSTANCE = this;
        EventBus.INSTANCE.register(new EventListener());
        return this;
    }

    @Override
    public Metadata getAddonMetadata() {
        AbstractAddon.Metadata metadata = new AbstractAddon.Metadata(this, "Auto Dab", "1.0", "Sk1er, KodingKing & SHARDcoder");
        metadata.setDisplayName(ChatColor.YELLOW + "AutoDab");
        metadata.setOverlayClassPath("cc.hyperium.addons.autodab.overlay.AutoDabOverlay");
        metadata.setDescription("A port of the forge mod Auto Dab.");

        return metadata;
    }

    public AutoDab() {
        this.currentlyDabbing = false;
    }

    boolean isCurrentlyDabbing() {
        return this.currentlyDabbing;
    }

    void setCurrentlyDabbing(final boolean currentlyDabbing) {
        this.currentlyDabbing = currentlyDabbing;
    }

}
