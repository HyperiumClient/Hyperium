package cc.hyperium.event;

/**
 * Invoked when the hud of the client is rendered
 */
public class RenderHUDEvent extends Event {

    private final float partialTicks;

    public RenderHUDEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
