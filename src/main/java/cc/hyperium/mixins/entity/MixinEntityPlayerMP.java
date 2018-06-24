package cc.hyperium.mixins.entity;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.LivingDeathEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP {
    /**
     * Death cannot be cancelled...
     * @author SiroQ
     **/
    @Inject(method = "onDeath",at = @At("HEAD"))
    private void onDeath(DamageSource source, CallbackInfo ci){
        EventBus.INSTANCE.post(new LivingDeathEvent((EntityLivingBase) (Object) this,source));
    }
}
