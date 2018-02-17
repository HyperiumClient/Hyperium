/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.mixins.entity;

import com.hcc.event.EventBus;
import com.hcc.event.PlayerSwingEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase{

    private boolean last = false;

    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "updateEntityActionState", at = @At("RETURN"))
    private void onUpdate(CallbackInfo ci){
        if(last != this.isSwingInProgress){
            last = this.isSwingInProgress;
            if(this.isSwingInProgress)
                 EventBus.INSTANCE.post(new PlayerSwingEvent(this.entityUniqueID, this.getPositionVector(), this.getLookVec(), this.getPosition()));
        }
    }

}
