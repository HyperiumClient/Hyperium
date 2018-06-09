/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.entity;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Shadow
    private Minecraft mc;

    /**
     * Uses server-side hit registration, instead of on the client
     *
     * @author boomboompower
     * @reason fixes a client-side particle issue
     */
    @Overwrite
    public void onEnchantmentCritical(Entity entityHit) {
        if (Minecraft.getMinecraft().isSingleplayer() || !Settings.CRIT_FIX) {
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
        if (Minecraft.getMinecraft().isSingleplayer() || !Settings.CRIT_FIX) {
            this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
        }
    }
}
