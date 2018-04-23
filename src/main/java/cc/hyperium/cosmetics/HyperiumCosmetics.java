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
    private DealWithItGlasses dealWithIt;
    private FlipCosmetic flipCosmetic;

    public HyperiumCosmetics() {
        register(new KillCounterMuscles());
        register(this.dabCosmetic = new DabOnKIllCosmetic());
        register(chromaWin = new ChromaWinCosmetic());
        register(flipCosmetic = new FlipCosmetic());
        register(dealWithIt = new DealWithItGlasses());
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

    public FlipCosmetic getFlipCosmetic() {
        return flipCosmetic;
    }
}
