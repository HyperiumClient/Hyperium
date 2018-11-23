package cc.hyperium.event;

/**
 * Invoked when the player levels up
 */
public final class LevelupEvent extends Event {

    private final int level;

    public LevelupEvent(int level) {
        this.level = level;
    }

    public final int getLevel() {
        return this.level;
    }
}
