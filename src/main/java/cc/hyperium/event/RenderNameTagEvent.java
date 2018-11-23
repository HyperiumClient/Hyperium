package cc.hyperium.event;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderNameTagEvent extends Event {

    public static boolean CANCEL = false;
    private AbstractClientPlayer entity;
    private RenderManager renderManager;

    public RenderNameTagEvent(AbstractClientPlayer player, RenderManager renderManager) {
        this.entity = player;
        this.renderManager = renderManager;

    }

    public RenderManager getRenderManager() {
        return renderManager;
    }

    public AbstractClientPlayer getEntity() {
        return entity;
    }

}
