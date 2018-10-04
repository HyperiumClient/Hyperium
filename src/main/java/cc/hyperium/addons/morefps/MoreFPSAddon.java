package cc.hyperium.addons.morefps;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.addons.morefps.utils.MoreFPSUtils;
import cc.hyperium.event.EventBus;

public class MoreFPSAddon extends AbstractAddon {

    @Override
    public AbstractAddon init() {
        EventBus.INSTANCE.register(this);
        Hyperium.CONFIG.register(new MoreFPSUtils());
        return this;
    }

    @Override
    public Metadata getAddonMetadata() {
        AbstractAddon.Metadata metadata = new AbstractAddon.Metadata(this, "MoreFPS", "1.1.1", "aycy");
        metadata.setDescription("Allows for better frames by disabling certain things.");
        return metadata;
    }
}
