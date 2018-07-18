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

import cc.hyperium.mixinsimp.entity.HyperiumEntityPlayerSP;
import cc.hyperium.mods.nickhider.NickHider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    private HyperiumEntityPlayerSP hyperiumEntityPlayerSP = new HyperiumEntityPlayerSP((EntityPlayerSP) (Object) this);

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
        hyperiumEntityPlayerSP.onEnchantmentCritical(entityHit,this.mc);
    }
    @ModifyVariable(method = "sendChatMessage",at=@At("HEAD"))
    public String sendChat(String chat) {
        NickHider instance = NickHider.INSTANCE;
        if(instance == null)
            return chat;
        return instance.out(chat);
    }
    /**
     * Uses server-side hit registration, instead of on the client
     *
     * @author boomboompower
     * @reason fixes a client-side particle issue
     */
    @Overwrite
    public void onCriticalHit(Entity entityHit) {
        hyperiumEntityPlayerSP.onCriticalHit(entityHit,mc);
    }
}
