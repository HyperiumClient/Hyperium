package cc.hyperium.mixinsimp.gui;

import cc.hyperium.gui.ParticleOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumInventoryParticle {
    private GuiContainer parent;

    public HyperiumInventoryParticle(GuiContainer parent) {
        this.parent = parent;
    }

    public void draw(int mouseX, int mouseY, float partialTicks, int guiLeft, int xSize, int guiTop, CallbackInfo ci) {

        ParticleOverlay overlay = ParticleOverlay.getOverlay();
        if (overlay.getMode() == ParticleOverlay.Mode.OFF)
            return;

        overlay.render(mouseX, mouseY, guiLeft - (Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty() ? 0 : xSize * 3 / 4), guiTop - 5, guiLeft + (240 * 4 / 5), guiTop + (240 * 4 / 5 - 10));

    }
}
