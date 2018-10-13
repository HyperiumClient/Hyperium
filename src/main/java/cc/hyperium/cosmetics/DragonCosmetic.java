package cc.hyperium.cosmetics;

import cc.hyperium.cosmetics.dragon.DragonHeadRenderer;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.client.entity.AbstractClientPlayer;

public class DragonCosmetic extends AbstractCosmetic {

    private DragonHeadRenderer renderer;

    public DragonCosmetic() {
        super(false, EnumPurchaseType.DRAGON_HEAD);
        renderer = new DragonHeadRenderer(this);
        EventBus.INSTANCE.register(renderer);
    }


    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent event) {
        AbstractClientPlayer player = event.getEntity();


    }
}
