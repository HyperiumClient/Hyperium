package cc.hyperium.event;

/**
 * Invoked once a world is being rendered
 */
public final class RenderWorldEvent extends Event {

    private final float partialTicks;

    public RenderWorldEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public final float getPartialTicks() {
        return this.partialTicks;
    }
}
