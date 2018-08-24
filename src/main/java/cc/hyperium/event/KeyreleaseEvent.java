package cc.hyperium.event;

/**
 * Invoked once a key is released
 */
public class KeyreleaseEvent extends Event {

    private final int key;
    private final boolean repeat;

    public KeyreleaseEvent(int key, boolean isRepeat) {
        this.key = key;
        this.repeat = isRepeat;
    }

    public int getKey() {
        return this.key;
    }

    public boolean isRepeat() {
        return this.repeat;
    }
}
