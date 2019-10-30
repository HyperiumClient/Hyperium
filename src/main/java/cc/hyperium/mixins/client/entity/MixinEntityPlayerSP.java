/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.client.entity;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.network.chat.SendChatMessageEvent;
import cc.hyperium.mods.nickhider.NickHider;
import cc.hyperium.utils.ChatUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow public float prevTimeInPortal;
    @Shadow public float timeInPortal;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    /**
     * @author FalseHonesty
     * @reason Post SendChatMessageEvent
     */
    @Overwrite
    public void sendChatMessage(String message) {
        SendChatMessageEvent event = new SendChatMessageEvent(message);
        EventBus.INSTANCE.post(event);

        if (!event.isCancelled()) {
            ChatUtil.sendMessage(message);
        }
    }

    /**
     * @author Sk1er
     * @reason NickHider
     */
    @ModifyVariable(method = "sendChatMessage", at = @At("HEAD"))
    private String sendChat(String chat) {
        NickHider instance = NickHider.instance;
        return instance == null || !instance.getNickHiderConfig().isMasterEnabled() ? chat : instance.out(chat);
    }

    /**
     * @author SiroQ
     * @reason Fix MC-7519
     */
    @Override
    public void removePotionEffectClient(int potionId) {
        if (potionId == Potion.confusion.id) {
            prevTimeInPortal = 0.0F;
            timeInPortal = 0.0F;
        }

        super.removePotionEffectClient(potionId);
    }
}
