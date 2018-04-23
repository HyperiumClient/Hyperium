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

package cc.hyperium.mixins.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.SendChatMessageEvent;
import cc.hyperium.mixins.packet.MixinC01PacketChatMessage;
import cc.hyperium.utils.ChatUtil;
import com.google.common.collect.ObjectArrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class MixinGuiChat {

    @Shadow
    protected GuiTextField inputField;

    public final Minecraft mc = Minecraft.getMinecraft();

    @Inject(method = "initGui", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        this.inputField.setMaxStringLength(256);
    }
    /**
     * Invoked when the player presses the enter key in the chat gui (before any processing is done)
     *
     * @param typedChar the typed char
     * @param keyCode   the key code
     * @param ci        {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     * @author boomboompower
     */
    @Inject(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;sendChatMessage(Ljava/lang/String;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void keyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        String msg = this.inputField.getText().trim();
        SendChatMessageEvent event = new SendChatMessageEvent(msg);
        EventBus.INSTANCE.post(event);
        if (!event.isCancelled()) {
            ChatUtil.sendMessage(msg);
        }
        this.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
        this.mc.displayGuiScreen(null);
        ci.cancel();
    }

    @Inject(method = "sendAutocompleteRequest", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetHandlerPlayClient;addToSendQueue(Lnet/minecraft/network/Packet;)V", shift = At.Shift.BEFORE))
    private void onSendAutocompleteRequest(String leftOfCursor, String fullInput, CallbackInfo ci) {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().autoComplete(leftOfCursor);
    }

    @ModifyArg(method = "onAutocompleteResponse", at = @At(value = "INVOKE", target = "Ljava/lang/String;equalsIgnoreCase(Ljava/lang/String;)Z"))
    private String removeChatFormattingOfCommonPrefix(String commonPrefix) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(commonPrefix);
    }

    /**
     * IntelliJ gives an error but it works and there are no errors in game, so don't question it
     */
    @ModifyArg(method = {"autocompletePlayerNames", "onAutocompleteResponse"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiTextField;writeText(Ljava/lang/String;)V"))
    private String removeChatFormattingOfCompletion(String completion) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(completion);
    }
}
