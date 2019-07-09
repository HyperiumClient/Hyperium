package cc.hyperium.mixins.entity;

import cc.hyperium.event.ItemPickupEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem {
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
}
