package cc.hyperium.event;

/**
 * Called when entities are about to be rendered in the world
 */
public final class RenderEntitiesEvent extends Event {

    private final float partialTicks;

    public RenderEntitiesEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public final float getPartialTicks() {
        return this.partialTicks;
    }
}
