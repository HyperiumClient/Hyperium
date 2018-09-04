package cc.hyperium.mixins.gui;

import cc.hyperium.event.ActionPerformedEvent;
import cc.hyperium.event.EventBus;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiInventory.class)
public class MixinGuiInventory {

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo info) {
        EventBus.INSTANCE.post(new ActionPerformedEvent((GuiScreen) (Object) this, button));
    }
}
