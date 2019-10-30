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

import cc.hyperium.mixinsimp.client.gui.HyperiumGuiScreenResourcePacks;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.ResourcePackListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.List;

/**
 * @author KodingKing
 */
@Mixin(GuiScreenResourcePacks.class)
public class MixinGuiScreenResourcePacks extends GuiScreen {

    @Shadow private GuiResourcePackAvailable availableResourcePacksList;
    @Shadow private GuiResourcePackSelected selectedResourcePacksList;
    @Shadow private List<ResourcePackListEntry> availableResourcePacks;

    private HyperiumGuiScreenResourcePacks hyperiumGuiResourcePack = new HyperiumGuiScreenResourcePacks(
        (GuiScreenResourcePacks) (Object) this);

    private GuiTextField textField;

    @Inject(method = "initGui", at = @At("HEAD"))
    private void initTextField(CallbackInfo ci) {
        String s1 = textField == null ? "" : textField.getText();
        textField = new GuiTextField(3, fontRendererObj, width / 2 - 4 - 200, height - 24, 200, 20);
        textField.setText(s1);
    }

    /**
     * @reason Change buttons size
     * @author SiroQ
     */
    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        hyperiumGuiResourcePack.initGui(buttonList);
    }

    /**
     * @reason Change text location
     * @author SiroQ
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        hyperiumGuiResourcePack.drawScreen(availableResourcePacksList, selectedResourcePacksList, mouseX, mouseY, partialTicks, fontRendererObj, width);
        super.drawScreen(mouseX, mouseY, partialTicks);
        textField.drawTextBox();
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        textField.mouseClicked(mouseX, mouseY, mouseButton);
        if (textField != null) textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (textField.isFocused()) textField.textboxKeyTyped(typedChar, keyCode);
        availableResourcePacksList = hyperiumGuiResourcePack.updateList(textField, availableResourcePacksList, availableResourcePacks, mc, width, height);
    }
}
