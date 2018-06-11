package cc.hyperium.cosmetics.Wings;

import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * @author ConorTheDev
 */

public class WingsCosmetic extends AbstractCosmetic {

    private Minecraft mc = Minecraft.getMinecraft();
    public ResourceLocation location;

    public int scale = 150;

    public WingsCosmetic() {
        super(true, EnumPurchaseType.WING_COSMETIC, true);

        if (Settings.wingsSELECTED.equals("Dragon Wings")) {
            location = new ResourceLocation("textures/cosmetics/wings/dragonwings.png");
        } else if (Settings.wingsSELECTED.equals("Angel Wings")) {
            location = new ResourceLocation("textures/cosmetics/wings/angelwings.png");
        }
    }

    //add custom colours soon
    public float[] getColours() {
        return new float[]{1.0f, 1.0f, 1.0f};
    }

    public void setScale(int yourScale) {
        this.scale = yourScale;
    }


}
