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

package cc.hyperium.cosmetics.companions.hamster;

import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.event.*;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.event.render.RenderEntitiesEvent;
import cc.hyperium.event.render.RenderPlayerEvent;
import cc.hyperium.event.world.WorldChangeEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
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

        if (!isPurchasedBy(uuid) || hamsters.containsKey(uuid) || toAdd.contains(e.getEntity())) return;

        HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(uuid);

        if (packageIfReady == null || packageIfReady.getCachedSettings().getCurrentCompanion() != EnumPurchaseType.HAMSTER_COMPANION) {
            return;
        }

        toAdd.add(e.getEntity());
    }

    @InvokeEvent
    public void onTick(TickEvent e) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld == null) return;

        toAdd.forEach(this::spawnHamster);
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
        return world.loadedEntityList.stream().anyMatch(entity -> entity.getUniqueID().equals(id));
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
