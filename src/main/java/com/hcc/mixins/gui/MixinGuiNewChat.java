package com.hcc.mixins.gui;

import com.hcc.event.ChatEvent;
import com.hcc.event.EventBus;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {

    /**
     * Invoked once a message is printed to the players chat
     * @param chatComponent
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "printChatMessage",at = @At("HEAD"))
    private void printChatMessage(IChatComponent chatComponent, CallbackInfo ci){
        EventBus.INSTANCE.post(new ChatEvent(chatComponent));
    }

}
