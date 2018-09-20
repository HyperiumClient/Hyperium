package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC<=10809
import net.minecraft.util.IChatComponent;
//#else
//$$ import net.minecraft.util.text.ITextComponent;
//#endif

import java.io.File;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Final
    @Shadow
    public File mcDataDir;
    @Shadow
    public int displayWidth;
    @Shadow
    public int displayHeight;
    @Shadow
    private Framebuffer framebufferMc;

    @Inject(
            method = "dispatchKeypresses",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    //#if MC<=10809
                    target = "Lnet/minecraft/client/gui/GuiNewChat;printChatMessage(Lnet/minecraft/util/IChatComponent;)V",
                    ordinal = 1
                    //#else
                    //$$ target = "Lnet/minecraft/client/gui/GuiNewChat;printChatMessage(Lnet/minecraft/util/text/ITextComponent;)V"
                    //#endif
            ),
            cancellable = true
    )
    private void dispatchKeypresses(CallbackInfo ci) {
        //#if MC<=10809
        IChatComponent chatComponent = ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc);
        //#else
        //$$ ITextComponent chatComponent = ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc);
        //#endif

        if (chatComponent != null) {
            new TextComponent(chatComponent).chat();
        }

        ci.cancel();
    }
}
