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

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiAddonError;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.GuiTos;
import cc.hyperium.gui.HyperiumMainMenu;
import cc.hyperium.internal.addons.AddonManifest;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    private boolean overLast = false;

    private boolean clickedCheckBox = false;

    /**
     * Override initGui
     *
     * @author Cubxity
     */
    @Overwrite
    public void initGui() {
        if (Hyperium.INSTANCE.isAcceptedTos()) {
            drawDefaultBackground();
        }
    }


    /**
     * TOS Rendering
     *
     * @author Sk1er and Kevin
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!Hyperium.INSTANCE.isAcceptedTos()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTos());
        } else if (!AddonMinecraftBootstrap.getDependenciesLoopMap().isEmpty() || !AddonMinecraftBootstrap.getMissingDependenciesMap().isEmpty()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiAddonError());
        } else {
            Minecraft.getMinecraft().displayGuiScreen(new HyperiumMainMenu());
        }
    }
}
