package cc.hyperium.handlers.handlers.tracking;

public enum ValueTrackingType {

    COINS("Coins", StatisticViewingGui.CompressionType.SUM),
    EXPERIENCE("Experience", StatisticViewingGui.CompressionType.SUM),
    RANKED_RATING("Ranked Rating", StatisticViewingGui.CompressionType.MAX),
    ERROR("Error", StatisticViewingGui.CompressionType.SUM);

    private String name;
    private StatisticViewingGui.CompressionType compressionType;
    ValueTrackingType(String name, StatisticViewingGui.CompressionType compressionType) {
        this.name = name;
        this.compressionType = compressionType;
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

    public StatisticViewingGui.CompressionType getCompressionType() {
        return compressionType;
    }
}
