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

package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumGuiScreenResourcePacks;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KodingKing
 */
@Mixin(GuiScreenResourcePacks.class)
public class MixinGuiScreenResourcePacks extends GuiScreen {

    @Shadow private GuiResourcePackAvailable availableResourcePacksList;
    @Shadow private GuiResourcePackSelected selectedResourcePacksList;

    private HyperiumGuiScreenResourcePacks hyperiumGuiResourcePack = new HyperiumGuiScreenResourcePacks(
        (GuiScreenResourcePacks) (Object) this);

    /**
     * @reason Change buttons size
     * @author SiroQ
     */
    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo callbackInfo) {
        hyperiumGuiResourcePack.initGui(this.buttonList);
    }

    /**
     * @reason Change text location
     * @author SiroQ
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        hyperiumGuiResourcePack.drawScreen(availableResourcePacksList, selectedResourcePacksList, mouseX, mouseY, partialTicks, fontRendererObj, width);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
