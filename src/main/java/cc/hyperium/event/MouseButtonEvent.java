package cc.hyperium.event;

/**
 * Invoked once a mouse button is pressed
 */
public class MouseButtonEvent extends Event {

    private final int value;
    private final boolean state;

    public MouseButtonEvent(int value, boolean state) {
        this.value = value;
        this.state = state;
    }

    public int getValue() {
        return this.value;
    }

    public boolean getState() {
        return this.state;
    }
}
