package cc.hyperium.cosmetics.companions.hamster;

import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.event.*;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.*;

public class HamsterCompanion extends AbstractCosmetic {
    private List<EntityPlayer> toAdd = new ArrayList<>();
    private Map<UUID, EntityHamster> hamsters = new HashMap<>();

    public HamsterCompanion() {
        super(false, EnumPurchaseType.HAMSTER_COMPANION);
    }

    @InvokeEvent
    public void renderEntities(RenderEntitiesEvent entitiesEvent) {
        if (Settings.SHOW_COMPANION_IN_1ST_PERSON) {
            renderPlayer(new RenderPlayerEvent(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().getRenderManager(), 0, 0, 0,
                entitiesEvent.getPartialTicks()));
        }
    }

    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent e) {
        if (Minecraft.getMinecraft().theWorld == null) return;
        UUID uuid = e.getEntity().getUniqueID();

        if (!isPurchasedBy(uuid)) return;
        if (hamsters.containsKey(uuid) || toAdd.contains(e.getEntity())) return;

        HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(uuid);

        if (packageIfReady == null) return;
        if (packageIfReady.getCachedSettings().getCurrentCompanion() != EnumPurchaseType.HAMSTER_COMPANION) return;

        toAdd.add(e.getEntity());
    }

    @InvokeEvent
    public void onTick(TickEvent e) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld == null) return;

        for (EntityPlayer player : toAdd) {
            spawnHamster(player);
        }

        toAdd.clear();

        Iterator<Map.Entry<UUID, EntityHamster>> ite = hamsters.entrySet().iterator();

        while (ite.hasNext()) {
            Map.Entry<UUID, EntityHamster> next = ite.next();
            if (!worldHasEntityWithUUID(theWorld, next.getKey())) {
                theWorld.unloadEntities(Collections.singletonList(next.getValue()));
                ite.remove();
            }
        }
    }

    @InvokeEvent
    public void onWorldChange(WorldChangeEvent e) {
        hamsters.clear();
    }

    public boolean worldHasEntityWithUUID(World world, UUID id) {
        for (Entity entity : world.loadedEntityList) {
            if (entity.getUniqueID().equals(id)) return true;
        }

        return false;
    }

    public void spawnHamster(EntityPlayer player) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;

        EntityHamster hamster = new EntityHamster(theWorld);
        hamster.setPosition(player.posX, player.posY, player.posZ);
        hamster.setOwnerId(player.getUniqueID().toString());

        theWorld.spawnEntityInWorld(hamster);

        hamsters.put(player.getUniqueID(), hamster);
    }
}
