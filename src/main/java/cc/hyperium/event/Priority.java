package cc.hyperium.event;

/**
 * Represents a priority
 */
public enum Priority {

    /**
     * High priority: This is called first, and is mostly used by internal code
     */
    HIGH(-1),

    /**
     * Normal priority: This is the default priority, and its the safest to use anywhere.
     */
    NORMAL(0),

    /**
     * Low priority: This is called last
     */
    LOW(1);

    public final int value;

    Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
