package cc.hyperium.purchases;

import cc.hyperium.config.Settings;
import cc.hyperium.utils.JsonHolder;

public class PurchaseSettings {
    private JsonHolder source;

    private boolean buttDisabled;
    private boolean wingsDisabled;
    private String wingsType;
    private double wingsScale;
    private boolean dragonHeadDisabled;
    private EnumPurchaseType currentHatType;
    private EnumPurchaseType companion;

    public PurchaseSettings(JsonHolder source) {
        this.source = source;

        this.buttDisabled = source.optJSONObject("butt").optBoolean("disabled");
        this.wingsDisabled = source.optJSONObject("wings").optBoolean("disabled");
        this.wingsType = source.optJSONObject("wings").optString("type");
        this.wingsScale = source.optJSONObject("wings")
            .optDouble("scale", Settings.WINGS_SCALE);
        dragonHeadDisabled = source.optJSONObject("dragon").optBoolean("disabled");
        this.currentHatType = EnumPurchaseType.parse(source.optJSONObject("hat").optString("current_type"));
        this.companion = EnumPurchaseType.parse(source.optJSONObject("companion").optString("type"));
    }

    public boolean isDragonHeadDisabled() {
        return dragonHeadDisabled;
    }

    public double getWingsScale() {
        return wingsScale;
    }

    public String getWingsType() {
        return wingsType;
    }

    public boolean isWingsDisabled() {
        return wingsDisabled;
    }

    public boolean isButtDisabled() {
        return buttDisabled;
    }


    public EnumPurchaseType getCurrentHatType() {
        return currentHatType;
    }

    public EnumPurchaseType getCurrentCompanion() {
        return companion;
    }
}
