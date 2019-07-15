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

package cc.hyperium.mixinsimp.client.entity;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.mixins.client.entity.IMixinAbstractClientPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumAbstractClientPlayer {
    private AbstractClientPlayer parent;

    public HyperiumAbstractClientPlayer(AbstractClientPlayer parent) {
        this.parent = parent;
    }

    public void init() {
        Hyperium.INSTANCE.getHandlers().getCapeHandler().getCape(parent);
    }

    public ResourceLocation getLocationCape() {
        ResourceLocation cape = Hyperium.INSTANCE.getHandlers().getCapeHandler().getCape(parent);
        if (cape != null)
            return cape;
        NetworkPlayerInfo networkplayerinfo = ((IMixinAbstractClientPlayer) parent).callGetPlayerInfo();
        return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
    }

    public void getFovModifier(CallbackInfoReturnable<Float> ci) {
        if (Settings.STATIC_FOV) {
            if (Minecraft.getMinecraft().thePlayer.isSprinting()
                && Settings.staticFovSprintModifier) {
                ci.setReturnValue((float) (
                    1.0f * (Minecraft.getMinecraft().thePlayer.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() * 1.300000011920929 / Minecraft.getMinecraft().thePlayer.capabilities.getWalkSpeed()
                        + 1.0) / 2.0));
            } else {
                ci.setReturnValue(1.0F);
            }
        }
    }
}
