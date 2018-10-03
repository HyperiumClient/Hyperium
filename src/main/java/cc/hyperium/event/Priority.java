package cc.hyperium.event;

public enum Priority {

    HIGH(-1), // Called first
    NORMAL(0),
    LOW(1); // Called last

    public final int value;

    private Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
