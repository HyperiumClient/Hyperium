package cc.hyperium.cosmetics;

import cc.hyperium.event.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public class HyperiumCosmetics {

    private List<AbstractCosmetic> cosmeticList = new ArrayList<>();

    public HyperiumCosmetics() {
        cosmeticList.add(new KillCounterMuscles());
    }

    private void register(AbstractCosmetic cosmetic) {
        cosmeticList.add(cosmetic);
        EventBus.INSTANCE.register(cosmetic);
    }
}
