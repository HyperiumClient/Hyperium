package cc.hyperium.mods.itemphysic;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;

/**
 * @author KodingKing
 */
public class ItemPhysicMod extends AbstractMod {

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(new EventHandlerLite());
        Hyperium.CONFIG.register(new ItemDummyContainer());

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Item Physic", "1.0", "CreativeMD");
    }
}
