package cc.hyperium.event;

import com.google.common.base.Preconditions;

import java.util.UUID;

import net.minecraft.entity.Entity;

import org.jetbrains.annotations.NotNull;

/**
 * Called when this player attacks an entity
 */
public final class PlayerAttackEntityEvent extends Event {

    @NotNull
    private final UUID uuid;

    @NotNull
    private final Entity entity;

    public PlayerAttackEntityEvent(@NotNull UUID uuid, @NotNull Entity entity) {
        Preconditions.checkNotNull(uuid, "uuid");
        Preconditions.checkNotNull(entity, "entity");

        this.uuid = uuid;
        this.entity = entity;
    }

    @NotNull
    public final UUID getUuid() {
        return this.uuid;
    }

    @NotNull
    public final Entity getEntity() {
        return this.entity;
    }
}
