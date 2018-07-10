/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.entity;

import cc.hyperium.mixinsimp.entity.HyperiumEntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {

    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Shadow
    public abstract boolean isPlayerSleeping();

    @Shadow
    public abstract Team getTeam();

    @Shadow
    public abstract String getName();

    private HyperiumEntityPlayer hyperiumEntityPlayer = new HyperiumEntityPlayer((EntityPlayer) (Object) this);

    @Inject(method = "updateEntityActionState", at = @At("RETURN"))
    private void onUpdate(CallbackInfo ci) {
        hyperiumEntityPlayer.onUpdate();
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"))
    public void attackTargetEntityWithCurrentItem(Entity targetEntity, CallbackInfo ci) {
        hyperiumEntityPlayer.attackTargetEntityWithCurrentItem(targetEntity);
    }

    /**
     * @author CoalOres
     */
    @Overwrite
    public float getEyeHeight() {
        return hyperiumEntityPlayer.getEyeHeight();
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public IChatComponent getDisplayName() {
        return hyperiumEntityPlayer.getDisplayName();
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onDeath(DamageSource source, CallbackInfo ci) {
        hyperiumEntityPlayer.onDeath(source);
    }
}
