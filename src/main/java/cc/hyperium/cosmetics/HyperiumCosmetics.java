/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.cosmetics;

import cc.hyperium.cosmetics.butt.ButtCosmetic;
import cc.hyperium.cosmetics.companions.dragon.DragonCompanion;
import cc.hyperium.cosmetics.companions.hamster.HamsterCompanion;
import cc.hyperium.cosmetics.deadmau5.Deadmau5Cosmetic;
import cc.hyperium.cosmetics.dragon.DragonCosmetic;
import cc.hyperium.cosmetics.flip.FlipCosmetic;
import cc.hyperium.cosmetics.hats.CosmeticHat;
import cc.hyperium.cosmetics.hats.ModelHatFez;
import cc.hyperium.cosmetics.hats.ModelHatLego;
import cc.hyperium.cosmetics.hats.ModelHatTophat;
import cc.hyperium.cosmetics.wings.WingsCosmetic;
import cc.hyperium.event.EventBus;
import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public class HyperiumCosmetics {

    private final List<AbstractCosmetic> cosmeticList = new ArrayList<>();
    private final FlipCosmetic flipCosmetic;
    private final Deadmau5Cosmetic deadmau5Cosmetic;
    private final WingsCosmetic wingsCosmetic;
    private final DragonCosmetic dragonCosmetic;
    private final ButtCosmetic buttCosmetic;
    private DragonCompanion dragonCompanion;
    private HamsterCompanion hamsterCompanion;
    private final CosmeticHat topHatCosmetic = new CosmeticHat(false, EnumPurchaseType.HAT_TOPHAT).setModel(new ModelHatTophat(), new ResourceLocation("textures/cosmetics/hats/tophat.png"));
    private final CosmeticHat fezCosmetic = new CosmeticHat(false, EnumPurchaseType.HAT_FEZ).setModel(new ModelHatFez(), new ResourceLocation("textures/cosmetics/hats/fez.png"));
    private final CosmeticHat legoCosmetic = new CosmeticHat(false, EnumPurchaseType.HAT_LEGO).setModel(new ModelHatLego(), new ResourceLocation("textures/cosmetics/hats/lego.png"));

    /**
     * Hyperium Cosmetics - Default Constructor/Cosmetic Registry
     */
    public HyperiumCosmetics() {
        registerCosmetic(buttCosmetic = new ButtCosmetic());
        registerCosmetic(flipCosmetic = new FlipCosmetic());
        registerCosmetic(deadmau5Cosmetic = new Deadmau5Cosmetic());
        registerCosmetic(wingsCosmetic = new WingsCosmetic());
        registerCosmetic(dragonCosmetic = new DragonCosmetic());
        registerCosmetic(dragonCompanion = new DragonCompanion());
        registerCosmetic(hamsterCompanion = new HamsterCompanion());
        registerCosmetic(topHatCosmetic);
        registerCosmetic(fezCosmetic);
        registerCosmetic(legoCosmetic);

    }

    public List<AbstractCosmetic> getCosmeticList() {
        return cosmeticList;
    }

    public DragonCompanion getDragonCompanion() {
        return dragonCompanion;
    }

    /**
     * Register Cosmetic - Register a Cosmetic Class
     *
     * @param cosmetic - Given Cosmetic Class
     */
    private void registerCosmetic(AbstractCosmetic cosmetic) {
        cosmeticList.add(cosmetic);
        EventBus.INSTANCE.register(cosmetic);
    }

    public HamsterCompanion getHamsterCompanion() {
        return hamsterCompanion;
    }

    /**
     * EnumCosmeticType - Used to distinguish cosmetics
     * and their types
     */
    public enum EnumCosmeticType {
        DAB,
        BUTT,
        FLIP,
        DEALWITHIT,
        DEADMAU5,
        WINGS,
        DRAGON,
        HAT_TOPHAT,
        HAT_FEZ,
        HAT_LEGO
    }

    /**
     * Get Cosmetic - Get a specific cosmetic from Enum Value
     *
     * @param givenType - Given Cosmetic Enum
     * @return - Given Abstract Cosmetic Class
     */
    public AbstractCosmetic getCosmetic(EnumCosmeticType givenType) {

        switch (givenType) {

            case BUTT:
                return buttCosmetic;
            case FLIP:
                return flipCosmetic;
            case DEADMAU5:
                return deadmau5Cosmetic;
            case WINGS:
                return wingsCosmetic;
            case DRAGON:
                return dragonCosmetic;
            case HAT_TOPHAT:
                return topHatCosmetic;
            case HAT_FEZ:
                return fezCosmetic;
            default:
                return null;

        }

    }

    public ButtCosmetic getButtCosmetic() {
        return buttCosmetic;
    }

    public DragonCosmetic getDragonCosmetic() {
        return dragonCosmetic;
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
}
