package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.utils.UpdateChecker;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen {

    @Inject(
            method = "drawScreen",
            at = @At(
                    value = "INVOKE",
                    //#if MC<=10809
                    target = "Lnet/minecraftforge/client/ForgeHooksClient;renderMainMenu(Lnet/minecraft/client/gui/GuiMainMenu;Lnet/minecraft/client/gui/FontRenderer;II)V",
                    //#else
                    //$$ target = "Lnet/minecraft/client/gui/GuiMainMenu;drawString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V",
                    //#endif
                    shift = At.Shift.BEFORE
            ),
            remap = false
    )
    private void onRender(int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo) {
        UpdateChecker.INSTANCE.drawUpdateMessage();
    }
}
