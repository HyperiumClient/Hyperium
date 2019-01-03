package cc.hyperium.handlers.handlers.tracking;

public class ValueTrackingItem {
    private ValueTrackingType type;
    private int value;
    private long time;

    public ValueTrackingItem(ValueTrackingType type, int value, long time) {
        this.type = type;
        this.value = value;
        this.time = time;
    }

    public ValueTrackingType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "ValueTrackingItem{" +
            "type=" + type +
            ", value=" + value +
            ", time=" + time +
            '}';
    }
}
