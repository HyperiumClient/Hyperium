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

import cc.hyperium.mixinsimp.gui.HyperiumGuiMainMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    private boolean overLast = false;

    private boolean clickedCheckBox = false;

    private HyperiumGuiMainMenu hyperiumGuiMainMenu = new HyperiumGuiMainMenu((GuiMainMenu) (Object) this);

    /**
     * Override initGui
     *
     * @author Cubxity
     */
    @Overwrite
    public void initGui() {
        hyperiumGuiMainMenu.initGui();
    }


    /**
     * TOS Rendering
     *
     * @author Sk1er and Kevin
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX,mouseY,partialTicks);
        hyperiumGuiMainMenu.drawScreen(mouseX,mouseY,partialTicks);
    }
}
