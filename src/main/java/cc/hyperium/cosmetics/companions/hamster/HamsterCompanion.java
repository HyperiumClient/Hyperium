package cc.hyperium.cosmetics.companions.hamster;

import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.event.WorldLoadEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class HamsterCompanion extends AbstractCosmetic {
    private boolean loadingWorld = false;

    public HamsterCompanion() {
        super(true, EnumPurchaseType.HAMSTER_COMPANION);
    }

    @InvokeEvent
    public void renderHud(RenderHUDEvent e) {
        if (!loadingWorld) return;
        loadingWorld = false;

        if (!isPurchasedBy(UUIDUtil.getClientUUID())) return;

        HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(UUIDUtil.getClientUUID());
        if (packageIfReady == null) return;

        if (packageIfReady.getCachedSettings().getCurrentCompanion() != EnumPurchaseType.HAMSTER_COMPANION) return;

        WorldClient world = Minecraft.getMinecraft().theWorld;
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;

        EntityHamster hamster = new EntityHamster(world);
        hamster.setPosition(thePlayer.posX, thePlayer.posY, thePlayer.posZ);

        world.spawnEntityInWorld(hamster);
    }

    @InvokeEvent
    public void onWorldChange(WorldLoadEvent e) {
        loadingWorld = true;
    }
}
