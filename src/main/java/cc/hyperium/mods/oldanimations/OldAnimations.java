package cc.hyperium.mods.oldanimations;

import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.utils.ChatColor;

public class OldAnimations extends AbstractMod {

    private final Metadata metadata;

    public OldAnimations() {
        Metadata data = new Metadata(this, "OldAnimations", "1.0", "Amplifiable");
        data.setDisplayName(ChatColor.AQUA + "OldAnimations");
        this.metadata = data;
    }

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(new AnimationEventHandler());
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return this.metadata;
    }
}
