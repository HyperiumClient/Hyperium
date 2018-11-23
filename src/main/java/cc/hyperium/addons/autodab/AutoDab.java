package cc.hyperium.addons.autodab;

import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.event.EventBus;
import cc.hyperium.utils.ChatColor;

/**
 * AutoDab implementation
 */
public class AutoDab extends AbstractAddon {

    /**
     * Whether the player is currently dabbing or not
     */
    private boolean currentlyDabbing;

    /**
     * Instance of AutoDab
     */
    static AutoDab INSTANCE;

    /**
     * Initialization of the addon. This is invoked on client start-up
     *
     * @return The addon instance
     */
    @Override
    public AbstractAddon init() {
        AutoDab.INSTANCE = this;
        EventBus.INSTANCE.register(new EventListener());
        return this;
    }

    /**
     * Returns the metadata of the addon. The metadata contains information about the
     * addon (author, version, etc.)
     *
     * @return The addon metadata
     */
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

    /**
     * Whether the user is currently dabbing or not
     *
     * @return Whether the user is currently dabbing or not
     */
    boolean isCurrentlyDabbing() {
        return this.currentlyDabbing;
    }

    /**
     * Sets whether the user is dabbing or not
     *
     * @param currentlyDabbing New value to set
     */
    void setCurrentlyDabbing(final boolean currentlyDabbing) {
        this.currentlyDabbing = currentlyDabbing;
    }

}
