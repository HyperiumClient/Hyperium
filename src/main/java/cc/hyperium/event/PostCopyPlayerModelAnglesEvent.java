package cc.hyperium.event;

import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;

import net.minecraft.client.entity.AbstractClientPlayer;

import org.jetbrains.annotations.NotNull;

/**
 * Get called after the angles of the upperleg gets copied into the lower leg etc
 * Edit the player rotation here, if the upperleg and the lowerleg need other roations
 */
public final class PostCopyPlayerModelAnglesEvent extends CopyPlayerModelAnglesEvent {

    public PostCopyPlayerModelAnglesEvent(@NotNull AbstractClientPlayer entity, @NotNull IMixinModelBiped model) {
        super(entity, model);
    }
}
