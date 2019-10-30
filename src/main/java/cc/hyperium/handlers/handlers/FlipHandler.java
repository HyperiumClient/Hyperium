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

package cc.hyperium.handlers.handlers;

import cc.hyperium.cosmetics.CosmeticsUtil;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.event.world.WorldChangeEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumChatFormatting;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FlipHandler {
    private final Map<UUID, Integer> rotateState = new ConcurrentHashMap<>();

    private int tick;

    @InvokeEvent
    public void tickEvent(TickEvent event) {
        tick++;
    }

    public void resetTick() {
        if (tick > 10)
            tick = 0;
    }

    @InvokeEvent
    public void swapWorld(WorldChangeEvent event) {
        UUID id = UUIDUtil.getClientUUID();
        if (id == null) return;
        Integer integer = rotateState.get(id);
        rotateState.clear();
        if (integer != null) rotateState.put(id, integer);
    }

    public void state(UUID uuid, int state) {
        rotateState.put(uuid, state);
    }

    public void transform(EntityLivingBase bat) {
        String s = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getName());
        Integer state = rotateState.get(bat.getUniqueID());
        if (CosmeticsUtil.shouldHide(EnumPurchaseType.FLIP_COSMETIC)) return;
        if ((state != null && state == 2) || s != null && (s.equals("Dinnerbone") ||
            s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer) bat).isWearing(EnumPlayerModelParts.CAPE))) {
            float y = bat.height + 0.1F;
            GlStateManager.translate(0.0F, y / 2, 0.0F);
            double l = System.currentTimeMillis() % (360 * 1.75) / 1.75;
            GlStateManager.rotate((float) l, .1F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -y / 2, 0.0F);
        } else if ((state != null && state == 1) || s != null && (s.equals("Dinnerbone") ||
            s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer) bat).isWearing(EnumPlayerModelParts.CAPE))) {
            {
                GlStateManager.translate(0.0F, bat.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    public int getSelf() {
        return get(UUIDUtil.getClientUUID());
    }

    public int get(UUID uuid) {
        return rotateState.getOrDefault(uuid, 0);
    }
}
