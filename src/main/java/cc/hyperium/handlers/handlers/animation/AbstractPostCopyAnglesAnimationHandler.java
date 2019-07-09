package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PostCopyPlayerModelAnglesEvent;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import net.minecraft.client.entity.AbstractClientPlayer;

public abstract class AbstractPostCopyAnglesAnimationHandler extends AbstractAnimationHandler {

    @InvokeEvent
    public void onPostCopyPlayerModelAngles(PostCopyPlayerModelAnglesEvent event) {
        AbstractClientPlayer entity = event.getEntity();
        IMixinModelBiped player = event.getModel();

        modify(entity, player, false);
    }
}
