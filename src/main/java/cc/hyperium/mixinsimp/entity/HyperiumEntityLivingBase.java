package cc.hyperium.mixinsimp.entity;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.LivingDeathEvent;
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

    public void getLook(float partialTicks, CallbackInfoReturnable<Vec3> ci, Vec3 look) {
        EntityLivingBase base = parent;
        if (base instanceof EntityPlayerSP) {
            ci.setReturnValue(look);
        }
    }

    public void onDeath(DamageSource source) {
        EventBus.INSTANCE.post(new LivingDeathEvent(parent, source));
    }
}
