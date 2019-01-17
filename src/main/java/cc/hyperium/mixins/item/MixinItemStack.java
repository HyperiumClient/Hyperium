package cc.hyperium.mixins.item;

import cc.hyperium.event.ItemTooltipEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Inject(method = "getTooltip", at = @At("RETURN"))
    private void injectTooltip(EntityPlayer playerIn, boolean advanced, CallbackInfoReturnable<List<String>> cir) {
        if (cir.getReturnValue() == null) return;

        new ItemTooltipEvent((ItemStack) (Object) this, cir.getReturnValue());
    }
}
