package cc.hyperium.mixins.item;

import cc.hyperium.event.ArrowLooseEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.LivingDeathEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBow.class)
public abstract class MixinItemBow {
    /**
     * Let's bow spam!... jk
     * @author SiroQ
     **/
    @Inject(method = "onPlayerStoppedUsing",at = @At("HEAD"))
    private void onDeath(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft, CallbackInfo ci){
        EventBus.INSTANCE.post(new ArrowLooseEvent(playerIn,stack,timeLeft));
    }
}
