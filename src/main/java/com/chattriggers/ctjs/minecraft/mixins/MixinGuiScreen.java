package com.chattriggers.ctjs.minecraft.mixins;

import cc.hyperium.Hyperium;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {
    @Inject(
            method = "sendChatMessage(Ljava/lang/String;Z)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSendChatMessage(String msg, boolean addToChat, CallbackInfo ci) {
        TriggerType.MESSAGE_SENT.triggerAll(ci, msg);
    }

    @Inject(
            method = "handleComponentClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;sendChatMessage(Ljava/lang/String;Z)V")
    )
    private void runCommand(IChatComponent p_175276_1_, CallbackInfoReturnable<Boolean> cir) {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().runningCommand = true;
    }
}
