package cc.hyperium.event.world;

import cc.hyperium.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityLeaveWorldEvent extends Event {

  private final World world;
  private final Entity entity;

  public EntityLeaveWorldEvent(World world, Entity entity) {
    this.world = world;
    this.entity = entity;
  }

  public World getWorld() {
    return world;
  }

  public Entity getEntity() {
    return entity;
  }
}
