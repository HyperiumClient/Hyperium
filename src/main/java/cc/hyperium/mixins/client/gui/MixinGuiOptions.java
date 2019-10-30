/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.client.gui;

import cc.hyperium.mixinsimp.client.gui.HyperiumGuiOptions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiOptions.class)
public class MixinGuiOptions extends GuiScreen {

    private HyperiumGuiOptions hyperiumGuiOptions = new HyperiumGuiOptions((GuiOptions) (Object) this);

    /**
     * @reason Snap done under the other top buttons
     */
    @Inject(method = "initGui", at = @At(value = "RETURN"))
    private void initGui(CallbackInfo c) {
        hyperiumGuiOptions.initGui(buttonList);
    }

    /**
     * @reason Add Hyperium Setting button
     */
    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void actionPerformed(GuiButton button, CallbackInfo c) {
        hyperiumGuiOptions.actionPerformed(button);
    }
}
