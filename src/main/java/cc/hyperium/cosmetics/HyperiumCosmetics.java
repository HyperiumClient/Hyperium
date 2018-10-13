package cc.hyperium.cosmetics;

import cc.hyperium.cosmetics.wings.WingsCosmetic;
import cc.hyperium.event.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public class HyperiumCosmetics {

    private final ChromaWinCosmetic chromaWin;
    private final List<AbstractCosmetic> cosmeticList = new ArrayList<>();
    private final DabOnKIllCosmetic dabCosmetic;
    private final FlipCosmetic flipCosmetic;
    private final Deadmau5Cosmetic deadmau5Cosmetic;
    private final WingsCosmetic wingsCosmetic;
    private final DealWithItGlasses dealWithIt;
    private final DragonCosmetic dragonCosmetic;
    private final ButtCosmetic buttCosmetic;

    public HyperiumCosmetics() {
        registerCosmetic(new KillCounterMuscles());
        registerCosmetic(dabCosmetic = new DabOnKIllCosmetic());

        registerCosmetic(chromaWin = new ChromaWinCosmetic());
        registerCosmetic(buttCosmetic = new ButtCosmetic());
        registerCosmetic(flipCosmetic = new FlipCosmetic());
        registerCosmetic(dealWithIt = new DealWithItGlasses());
        registerCosmetic(deadmau5Cosmetic = new Deadmau5Cosmetic());
        registerCosmetic(wingsCosmetic = new WingsCosmetic());
        registerCosmetic(dragonCosmetic = new DragonCosmetic());
    }

    public ButtCosmetic getButtCosmetic() {
        return buttCosmetic;
    }

    public DragonCosmetic getDragonCosmetic() {
        return dragonCosmetic;
    }

    /**
     * Register Cosmetic - Register a Cosmetic Class
     * @param cosmetic - Given Cosmetic Class
     */
    private void registerCosmetic(AbstractCosmetic cosmetic) {
        cosmeticList.add(cosmetic);
        EventBus.INSTANCE.register(cosmetic);
    }

    /**
     * Get Cosmetic - Get a specific cosmetic from Enum Value
     * @param givenType - Given Cosmetic Enum
     * @return - Given Abstract Cosmetic Class
     */
    public AbstractCosmetic getCosmetic(EnumCosmeticType givenType){

        switch(givenType){

            case DAB:
                return dabCosmetic;
            case CHROMA_WIN:
                return chromaWin;
            case BUTT:
                return buttCosmetic;
            case FLIP:
                return flipCosmetic;
            case DEALWITHIT:
                return dealWithIt;
            case DEADMAU5:
                return deadmau5Cosmetic;
            case WINGS:
                return wingsCosmetic;
            case DRAGON:
                return dragonCosmetic;
            default:
                return null;

        }

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

    public Deadmau5Cosmetic getDeadmau5Cosmetic() {
        return deadmau5Cosmetic;
    }

    public WingsCosmetic getWingsCosmetic() {
        return wingsCosmetic;
    }

    /**
     * EnumCosmeticType - Used to distinguish cosmetics
     * and their types
     */
    public enum EnumCosmeticType {

        DAB,
        CHROMA_WIN,
        BUTT,
        FLIP,
        DEALWITHIT,
        DEADMAU5,
        WINGS,
        DRAGON

    }

}
