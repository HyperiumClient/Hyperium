package cc.hyperium.event;

public final class HypixelGetXPEvent extends Event {

    private final int xp;

    public HypixelGetXPEvent(int xp) {
        this.xp = xp;
    }

    public final int getXp() {
        return this.xp;
    }
}
