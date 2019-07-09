package cc.hyperium.mixins.entity;

import cc.hyperium.mixinsimp.entity.HyperiumEntityPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
public class MixinEntityPlayerMP {

    private HyperiumEntityPlayerMP hyperiumEntityPlayerMP = new HyperiumEntityPlayerMP((EntityPlayerMP) (Object) this);

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onDeath(DamageSource source, CallbackInfo ci) {
        hyperiumEntityPlayerMP.onDeath(source);
    }
}
