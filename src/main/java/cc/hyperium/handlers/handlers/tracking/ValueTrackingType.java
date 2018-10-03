package cc.hyperium.handlers.handlers.tracking;

public enum ValueTrackingType {

    COINS("Coins", StatisticViewingGui.CompressionType.SUM, StatisticViewingGui.MissingDataHandling.ZERO),
    EXPERIENCE("Experience", StatisticViewingGui.CompressionType.SUM, StatisticViewingGui.MissingDataHandling.ZERO),
    RANKED_RATING("Ranked Rating", StatisticViewingGui.CompressionType.MAX, StatisticViewingGui.MissingDataHandling.AVERAGE),
    ERROR("Error", StatisticViewingGui.CompressionType.SUM, StatisticViewingGui.MissingDataHandling.ZERO);

    private String name;
    private StatisticViewingGui.CompressionType compressionType;
    private StatisticViewingGui.MissingDataHandling missingDataHandling;

    ValueTrackingType(String name, StatisticViewingGui.CompressionType compressionType, StatisticViewingGui.MissingDataHandling missingDataHandling) {
        this.name = name;
        this.compressionType = compressionType;
        this.missingDataHandling = missingDataHandling;
    }

    public static ValueTrackingType parse(String in) {
        try {
            valueOf(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERROR;
    }

    public StatisticViewingGui.MissingDataHandling getMissingDataHandling() {
        return missingDataHandling;
    }

    public String getDisplay() {
        return name;
    }

    public StatisticViewingGui.CompressionType getCompressionType() {
        return compressionType;
    }
}
