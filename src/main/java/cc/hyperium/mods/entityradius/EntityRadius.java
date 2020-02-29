package cc.hyperium.mods.entityradius;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.EntityRenderEvent;
import cc.hyperium.mods.AbstractMod;
import net.minecraft.client.Minecraft;

public class EntityRadius extends AbstractMod {

  /**
   * The init method where all events and commands should be registered. Use this to load configs as
   * well
   *
   * @return the {@link AbstractMod} instance of the mod
   */
  @Override
  public AbstractMod init() {
    EventBus.INSTANCE.register(this);
    return this;
  }

  @InvokeEvent
  public void renderEntity(EntityRenderEvent event) {
    if (Settings.ENABLE_ENTITY_RADIUS) {
      if (event.getEntityIn().getDistanceToEntity(Minecraft.getMinecraft().thePlayer)
          >= Settings.ENTITY_RADIUS) {
        event.setCancelled(true);
      }
    }
  }

  /**
   * This mods metadata, which will be displayed in the configuration gui and other places
   *
   * @return the mods metadata
   */
  @Override
  public Metadata getModMetadata() {
    return new Metadata(this, "Entity Radius", "1.0", "asbyth");
  }
}
