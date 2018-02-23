/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.mixins.gui;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.SendChatMessageEvent;
import cc.hyperium.mixins.packet.MixinC01PacketChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiChat.class)
public class MixinGuiChat {

    @Shadow
    protected GuiTextField inputField;

    @Shadow
    private boolean playerNamesFound;
    @Shadow
    private List<String> foundPlayerNames;
    private final Minecraft mc = Minecraft.getMinecraft();

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
            final C01PacketChatMessage packet = new C01PacketChatMessage(msg);
            ((MixinC01PacketChatMessage) packet).setMessage(msg);
            this.mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        this.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
        this.mc.displayGuiScreen(null);
        ci.cancel();
    }

    // TODO
   /* @Inject(method = "autocompletePlayerNames", at = @At(value = "JUMP", target = "Lnet/minecraft/client/gui/GuiChat;autocompletePlayerNames()V"), require = 1)
    private void autocomplete(CallbackInfo ci) {
        System.out.println("test");
    }*/


}
