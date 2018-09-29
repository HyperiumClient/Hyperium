package cc.hyperium.event;

import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;

import com.google.common.base.Preconditions;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class CopyPlayerModelAnglesEvent extends Event {

    @NotNull
    private final AbstractClientPlayer entity;
    @NotNull
    private final IMixinModelBiped model;

    public CopyPlayerModelAnglesEvent(@NotNull AbstractClientPlayer entity, @NotNull IMixinModelBiped model) {
        Preconditions.checkNotNull(entity, "entity");
        Preconditions.checkNotNull(model, "model");

        this.entity = entity;
        this.model = model;
    }

    @NotNull
    public final AbstractClientPlayer getEntity() {
        return this.entity;
    }

    @NotNull
    public final IMixinModelBiped getModel() {
        return this.model;
    }
}
