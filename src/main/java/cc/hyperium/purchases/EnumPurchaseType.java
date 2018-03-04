package cc.hyperium.purchases;

public enum EnumPurchaseType {
    LEVEL_HEAD(2709838),
    EARLY_BIRD(2888258),
    //set to 0 because 0 is default value of a primitive !
    UNKNOWN(0);

    private int id;

    EnumPurchaseType(int i) {
        this.id = i;
    }

    public static EnumPurchaseType parse(String asString) {
        try {
            return valueOf(asString.toUpperCase());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public int getId() {
        return id;
    }
}
