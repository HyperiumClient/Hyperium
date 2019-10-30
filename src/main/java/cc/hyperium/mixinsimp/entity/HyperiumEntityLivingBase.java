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

package cc.hyperium.mixinsimp.entity;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.entity.LivingDeathEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumEntityLivingBase {

    private EntityLivingBase parent;

    public HyperiumEntityLivingBase(EntityLivingBase parent) {
        this.parent = parent;
    }

    public void getLook(CallbackInfoReturnable<Vec3> ci, Vec3 look) {
        EntityLivingBase base = parent;
        if (base instanceof EntityPlayerSP) ci.setReturnValue(look);
    }

    public void onDeath(DamageSource source) {
        EventBus.INSTANCE.post(new LivingDeathEvent(parent, source));
    }
}
