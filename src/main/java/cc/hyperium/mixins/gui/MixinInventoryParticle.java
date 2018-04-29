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

import cc.hyperium.gui.ParticleOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)

public class MixinInventoryParticle {
    @Shadow
    private int guiLeft;
    @Shadow
    private int guiTop;

    @Shadow
    private int xSize;

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGuiContainerBackgroundLayer(FII)V"))
    private void draw(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {

        ParticleOverlay overlay = ParticleOverlay.getOverlay();
        if (overlay.getMode() == ParticleOverlay.Mode.OFF)
            return;

        overlay.render(mouseX, mouseY, guiLeft - (Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty() ? 0 : xSize * 3 / 4), guiTop - 5, guiLeft + (240 * 4 / 5), guiTop + (240 * 4 / 5 - 10));

    }
}
