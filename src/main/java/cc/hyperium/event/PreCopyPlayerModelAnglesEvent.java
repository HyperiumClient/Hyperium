package cc.hyperium.event;

import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;

import net.minecraft.client.entity.AbstractClientPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Get called before the angles of the upperleg gets copied into the lower leg etc
 * Edit the player rotation here, if the upperleg and the lowerleg need the same roations
 */
public final class PreCopyPlayerModelAnglesEvent extends CopyPlayerModelAnglesEvent {

    public PreCopyPlayerModelAnglesEvent(@NotNull AbstractClientPlayer entity, @NotNull IMixinModelBiped model) {
        super(entity, model);

    }
}
