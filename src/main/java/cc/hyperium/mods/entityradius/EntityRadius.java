package cc.hyperium.mods.entityradius;

import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.EntityRenderEvent;
import net.minecraft.client.Minecraft;

public class EntityRadius extends AbstractAddon {

    @Override
    public AbstractAddon init() {
        EventBus.INSTANCE.register(this);
        return this;
    }

    @InvokeEvent
    public void renderEntity(EntityRenderEvent event) {
        if (Settings.ENABLE_ENTITY_RADIUS) {
            if (event.getEntityIn().getDistanceToEntity(Minecraft.getMinecraft().thePlayer) >= Settings.ENTITY_RADIUS) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public Metadata getAddonMetadata() {
        return new Metadata(this, "Entity Radius", "1.0", "asbyth");
    }
}
