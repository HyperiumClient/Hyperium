package cc.hyperium.cosmetics.wings;

import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.event.EventBus;
import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * @author ConorTheDev
 */

public class WingsCosmetic extends AbstractCosmetic {

    private final ResourceLocation dragon = new ResourceLocation("textures/cosmetics/wings/dragonwings.png");
    private Minecraft mc = Minecraft.getMinecraft();
    private WingsRenderer wingsRenderer;

    public WingsCosmetic() {
        super(true, EnumPurchaseType.WING_COSMETIC);
        EventBus.INSTANCE.register(wingsRenderer = new WingsRenderer(this));

    }

    public WingsRenderer getWingsRenderer() {
        return wingsRenderer;
    }

    //add custom colours soon
    public float[] getColours() {
        return new float[]{1.0f, 1.0f, 1.0f};
    }


    public ResourceLocation getLocation(String s) {
        if (s == null)
            return dragon;
        switch (s) {
            case "Dragon wings":
                return dragon;
            default:
                return dragon;
        }
    }
}
