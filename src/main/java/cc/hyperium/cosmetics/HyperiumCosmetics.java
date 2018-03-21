package cc.hyperium.cosmetics;

import cc.hyperium.event.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public class HyperiumCosmetics {

    private final ChromaWinCosmetic chromaWin;
    private List<AbstractCosmetic> cosmeticList = new ArrayList<>();
    private DabOnKIllCosmetic dabCosmetic;

    public HyperiumCosmetics() {
        register(new KillCounterMuscles());
        register(this.dabCosmetic = new DabOnKIllCosmetic());
        register(chromaWin = new ChromaWinCosmetic());
    }

    private void register(AbstractCosmetic cosmetic) {
        cosmeticList.add(cosmetic);
        EventBus.INSTANCE.register(cosmetic);
    }

    public DabOnKIllCosmetic getDabCosmetic() {
        return dabCosmetic;
    }

    public ChromaWinCosmetic getChromaWin() {
        return chromaWin;
    }
}
