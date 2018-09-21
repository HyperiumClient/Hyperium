package cc.hyperium.handlers.handlers.tracking;

public enum ValueTrackingType {

    COINS("Coins"),
    EXPERIENCE("Experience"),
    RANKED_RATING("Ranked Rating"),
    ERROR("Error");

    private String name;

    ValueTrackingType(String name) {
        this.name = name;
    }

    public static ValueTrackingType parse(String in) {
        try {
            valueOf(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERROR;
    }

    public String getDisplay() {
        return name;
    }
}
