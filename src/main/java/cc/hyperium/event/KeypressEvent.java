package cc.hyperium.event;

/**
 * Invoked once a key is pressed
 */
public class KeypressEvent extends Event {

    private final int key;
    private final boolean repeat;

    public KeypressEvent(int key, boolean isRepeat) {
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
