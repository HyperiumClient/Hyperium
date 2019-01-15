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

import cc.hyperium.event.EventBus;
import cc.hyperium.event.SendChatMessageEvent;
import cc.hyperium.mixinsimp.entity.HyperiumEntityPlayerSP;
import cc.hyperium.mods.nickhider.NickHider;
import com.mojang.authlib.GameProfile;
import cc.hyperium.utils.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends AbstractClientPlayer {

    private HyperiumEntityPlayerSP hyperiumEntityPlayerSP = new HyperiumEntityPlayerSP((EntityPlayerSP) (Object) this);

    @Shadow
    private Minecraft mc;

    @Shadow
    public float prevTimeInPortal;

    @Shadow
    public float timeInPortal;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    /**
     * Uses server-side hit registration, instead of on the client
     *
     * @author boomboompower
     * @reason fixes a client-side particle issue
     */
    @Overwrite
    public void onEnchantmentCritical(Entity entityHit) {
        hyperiumEntityPlayerSP.onEnchantmentCritical(entityHit, this.mc);
    }

    /**
     * @author False Honesty & Sk1er
     * @reason Post SendChatMessageEvent & NickHider
     */
    @Overwrite
    public void sendChatMessage(String message) {
        NickHider instance = NickHider.INSTANCE;
        if (instance != null)
            message = instance.out(message);

        SendChatMessageEvent event = new SendChatMessageEvent(message);
        EventBus.INSTANCE.post(event);

        if (!event.isCancelled()) {
            ChatUtil.sendMessage(message);
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
        hyperiumEntityPlayerSP.onCriticalHit(entityHit, mc);
    }

    /**
     * @reason Fix MC-7519
     * @author SiroQ
     */
    @Override
    public void removePotionEffectClient(int potionId) {
        if (potionId == Potion.confusion.id) {
            this.prevTimeInPortal = 0.0F;
            this.timeInPortal = 0.0F;
        }
        super.removePotionEffectClient(potionId);
    }
}
