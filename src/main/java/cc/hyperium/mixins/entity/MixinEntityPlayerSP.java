package cc.hyperium.mixins.entity;

import cc.hyperium.gui.settings.items.GeneralSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    
    @Shadow protected Minecraft mc;
    
    /**
     * Uses server-side hit registration, instead of on the client
     *
     * @author boomboompower
     * @reason fixes a client-side particle issue
     */
    @Overwrite
    public void onEnchantmentCritical(Entity entityHit) {
        if (Minecraft.getMinecraft().isSingleplayer() || !GeneralSetting.combatParticleFixEnabled) {
            this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
        }
    }
    
    /**
     * Uses server-side hit registration, instead of on the client
     *
     * @author boomboompower
     * @reason fixes a client-side particle issue
     */
    @Overwrite
    public void onCriticalHit(Entity entityHit) {
        if (Minecraft.getMinecraft().isSingleplayer() || !GeneralSetting.combatParticleFixEnabled) {
            this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
        }
    }
}
