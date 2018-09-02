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

import cc.hyperium.mixinsimp.gui.HyperiumGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Shadow
    private Minecraft mc;
    private HyperiumGuiScreen hyperiumGuiScreen = new HyperiumGuiScreen((GuiScreen) (Object) this);

    @Shadow
    protected abstract void setText(String newChatText, boolean shouldOverwrite);

    @Shadow
    protected abstract void actionPerformed(GuiButton button) throws IOException;

    @Inject(method = "drawWorldBackground", at = @At("HEAD"), cancellable = true)
    private void drawWorldBackground(int tint, CallbackInfo ci) {
        hyperiumGuiScreen.drawWorldBackground(tint, this.mc, ci);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        hyperiumGuiScreen.mouseClicked(mouseX, mouseY, mouseButton, ci);
    }

    @Inject(method = "initGui", at = @At("HEAD"))
    private void initGui(CallbackInfo ci) {
        hyperiumGuiScreen.initGui();
    }

    @Inject(method = "onGuiClosed", at = @At("HEAD"))
    private void onGuiClosed(CallbackInfo ci) {
        hyperiumGuiScreen.onGuiClosed(ci);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void actionPerformed(GuiButton button, CallbackInfo info) {
        System.out.println("k");
        if (hyperiumGuiScreen.actionPerformed(button))
            info.cancel();
    }

}
