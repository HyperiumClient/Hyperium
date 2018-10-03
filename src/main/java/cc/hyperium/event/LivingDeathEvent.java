package cc.hyperium.event;

import com.google.common.base.Preconditions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

import org.jetbrains.annotations.NotNull;

public final class LivingDeathEvent {

    @NotNull
    private final EntityLivingBase entity;

    @NotNull
    private final DamageSource cause;

    public LivingDeathEvent(@NotNull EntityLivingBase entity, @NotNull DamageSource cause) {
        Preconditions.checkNotNull(entity, "entity");
        Preconditions.checkNotNull(cause, "cause");

        this.entity = entity;
        this.cause = cause;
    }

    @NotNull
    public final EntityLivingBase getEntity() {
        return this.entity;
    }

    @NotNull
    public final DamageSource getCause() {
        return this.cause;
    }
}
