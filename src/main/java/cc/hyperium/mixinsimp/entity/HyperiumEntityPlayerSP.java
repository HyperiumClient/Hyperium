package cc.hyperium.mixinsimp.entity;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;

public class HyperiumEntityPlayerSP {
    private EntityPlayerSP parent;

    public HyperiumEntityPlayerSP(EntityPlayerSP parent) {
        this.parent = parent;
    }

    public void onEnchantmentCritical(Entity entityHit, Minecraft mc) {
        if (mc.isSingleplayer() || !Settings.CRIT_FIX) {
            mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
        }
    }

    public void onCriticalHit(Entity entityHit, Minecraft mc) {
        if (Minecraft.getMinecraft().isSingleplayer() || !Settings.CRIT_FIX) {
            mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
        }
    }
}
