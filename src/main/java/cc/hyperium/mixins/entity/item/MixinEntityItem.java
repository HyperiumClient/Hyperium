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

package cc.hyperium.mixins.entity.item;

import cc.hyperium.event.world.item.ItemPickupEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem {

    @Shadow public abstract ItemStack getEntityItem();

    @Inject(
        method = "onCollideWithPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/item/EntityItem;getEntityItem()Lnet/minecraft/item/ItemStack;"
        )
    )
    private void pickupItem(EntityPlayer player, CallbackInfo ci) {
        new ItemPickupEvent(player, (EntityItem) (Object) this).post();
    }

    @Inject(method = "searchForOtherItemsNearby", at = @At("HEAD"), cancellable = true)
    private void stopSearchWhenFullStack(CallbackInfo ci) {
        ItemStack stack = getEntityItem();
        if (stack.stackSize > stack.getMaxStackSize()) {
            ci.cancel();
        }
    }

    @Inject(method = "combineItems", at = @At("HEAD"), cancellable = true)
    private void stopAttemptingToCombine(EntityItem other, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = getEntityItem();
        if (stack.stackSize > stack.getMaxStackSize()) {
            cir.setReturnValue(false);
        }
    }
}
