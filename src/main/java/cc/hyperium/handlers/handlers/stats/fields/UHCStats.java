package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import net.hypixel.api.GameType;

public class UHCStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "UHC-64";
    }

    @Override
    public String getName() {
        return GameType.UHC.getName();
    }
}
