package com.hcc.mixins.gui;

import com.hcc.event.EventBus;
import com.hcc.event.ServerLeaveEvent;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDisconnected.class)
public class MixinGuiDisconnecting {

    /**
     * Invoked once the player is discconecting from a server
     * @param screen
     * @param reasonLocalizationKey
     * @param chatComp
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "<init>",at = @At("RETURN"))
    private void init(GuiScreen screen,
                      String reasonLocalizationKey,
                      IChatComponent chatComp,
                      CallbackInfo ci){
        EventBus.INSTANCE.post(new ServerLeaveEvent());
    }
}