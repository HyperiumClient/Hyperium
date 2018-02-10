package com.hcc.mixins.gui;

import com.hcc.event.EventBus;
import com.hcc.event.ServerJoinEvent;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting {

    /**
     * Invoked once the player is connecting to a server
     * @param ip the address they are connecting to
     * @param port the port they are connecting to
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "connect",at = @At("HEAD"))
    private void connect(String ip, int port, CallbackInfo ci){
        EventBus.INSTANCE.post(new ServerJoinEvent(ip, port));
    }
}