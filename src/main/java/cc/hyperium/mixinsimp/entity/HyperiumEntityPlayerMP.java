package cc.hyperium.mixinsimp.entity;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.LivingDeathEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;

public class HyperiumEntityPlayerMP {
    private EntityPlayerMP parent;

    public HyperiumEntityPlayerMP(EntityPlayerMP parent) {
        this.parent = parent;
    }

    public void onDeath(DamageSource source) {
        EventBus.INSTANCE.post(new LivingDeathEvent(parent, source));
    }
}
