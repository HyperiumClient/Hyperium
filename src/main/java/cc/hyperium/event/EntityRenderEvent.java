package cc.hyperium.event;

import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class EntityRenderEvent extends CancellableEvent {

    @NotNull
    private final Entity entityIn;
    private final float posX;
    private final float posY;
    private final float posZ;
    private final float pitch;
    private final float yaw;
    private final float scale;

    public EntityRenderEvent(@NotNull Entity entityIn, float posX, float posY, float posZ, float pitch, float yaw, float scale) {
        Preconditions.checkNotNull(entityIn, "entityIn");

        this.entityIn = entityIn;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.pitch = pitch;
        this.yaw = yaw;
        this.scale = scale;
    }

    @NotNull
    public final Entity getEntityIn() {
        return this.entityIn;
    }

    public final float getPosX() {
        return this.posX;
    }

    public final float getPosY() {
        return this.posY;
    }

    public final float getPosZ() {
        return this.posZ;
    }

    public final float getPitch() {
        return this.pitch;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final float getScale() {
        return this.scale;
    }
}
