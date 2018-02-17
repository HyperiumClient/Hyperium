/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.mixins.gui;

import com.hcc.event.EventBus;
import com.hcc.event.SendChatMessageEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class MixinGuiChat {

    @Shadow protected GuiTextField inputField;

    private final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Invoked when the player presses the enter key in the chat gui (before any processing is done)
     *
     * @author boomboompower
     *
     * @param typedChar the typed char
     * @param keyCode the key code
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;sendChatMessage(Ljava/lang/String;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void keyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        SendChatMessageEvent event = new SendChatMessageEvent(this.inputField.getText().trim(), true);
        EventBus.INSTANCE.post(event);
        if (event.isCancelled()) {
            ci.cancel();

            if (event.getAddsToClientHistory()) {
                this.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
            }
        }
    }
}
