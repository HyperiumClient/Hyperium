package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PreCopyPlayerModelAnglesEvent;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import net.minecraft.client.entity.AbstractClientPlayer;

public abstract class AbstractPreCopyAnglesAnimationHandler extends AbstractAnimationHandler {

    @InvokeEvent
    public void onPreCopyPlayerModelAngles(PreCopyPlayerModelAnglesEvent event) {
        AbstractClientPlayer entity = event.getEntity();
        IMixinModelBiped player = event.getModel();

        modify(entity, player, true);
    }
}
