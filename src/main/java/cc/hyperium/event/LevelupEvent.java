package cc.hyperium.event;

public final class LevelupEvent {

    private final int level;

    public LevelupEvent(int level) {
        this.level = level;
    }

    public final int getLevel() {
        return this.level;
    }
}
