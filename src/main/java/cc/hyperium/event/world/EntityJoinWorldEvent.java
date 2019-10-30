package cc.hyperium.event.world;

import cc.hyperium.event.CancellableEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityJoinWorldEvent extends CancellableEvent {

    private final World world;
    private final Entity entity;

    public EntityJoinWorldEvent(World world, Entity entity) {
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
