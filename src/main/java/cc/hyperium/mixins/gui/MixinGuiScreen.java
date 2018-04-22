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

import cc.hyperium.event.EventBus;
import cc.hyperium.event.GuiClickEvent;
import cc.hyperium.gui.settings.items.BackgroundSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Shadow protected Minecraft mc;
    protected GuiScreen instance = (GuiScreen) (Object) this;

    @Inject(method = "drawWorldBackground", at = @At("HEAD"), cancellable = true)
    private void drawWorldBackground(int tint, CallbackInfo ci) {
        if (this.mc.theWorld != null && BackgroundSettings.fastWorldGuiEnabled) {
            ci.cancel();
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        final GuiClickEvent event = new GuiClickEvent(mouseX, mouseY, mouseButton, instance);
        EventBus.INSTANCE.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

}
