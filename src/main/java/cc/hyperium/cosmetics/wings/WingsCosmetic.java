package cc.hyperium.cosmetics.wings;

import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.event.EventBus;
import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * @author ConorTheDev
 */

public class WingsCosmetic extends AbstractCosmetic {

    public ResourceLocation location;
    public int scale = 150;
    private Minecraft mc = Minecraft.getMinecraft();
    private WingsRenderer wingsRenderer;

    public WingsCosmetic() {
        super(true, EnumPurchaseType.WING_COSMETIC, true);
        if (Settings.wingsSELECTED.equals("Dragon wings")) {
            location = new ResourceLocation("textures/cosmetics/wings/dragonwings.png");
        } else if (Settings.wingsSELECTED.equals("Angel wings")) {
            location = new ResourceLocation("textures/cosmetics/wings/angelwings.png");
        }
        EventBus.INSTANCE.register(wingsRenderer = new WingsRenderer(this));

    }

    public WingsRenderer getWingsRenderer() {
        return wingsRenderer;
    }

    //add custom colours soon
    public float[] getColours() {
        return new float[]{1.0f, 1.0f, 1.0f};
    }

    public void setScale(int yourScale) {
        this.scale = yourScale;
    }


}
