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

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.internal.addons.AddonManifest;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import cc.hyperium.utils.ChatColor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;

public class GuiAddonError extends GuiScreen {
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int textY = 20;
        drawCenteredString(fontRendererObj, ChatColor.RED.toString() + ChatColor.BOLD + "ERROR LOADING ADDONS", width / 2, textY, Color.WHITE.getRGB());
        textY += 10;
        drawCenteredString(fontRendererObj, ChatColor.RED.toString() + ChatColor.BOLD + "THE FOLLOWING ADDONS WON'T LOAD", width / 2, textY, Color.WHITE.getRGB());
        textY += 10;
        if (!AddonMinecraftBootstrap.getMissingDependenciesMap().isEmpty()) {
            for (Map.Entry<AddonManifest, ArrayList<String>> entry : AddonMinecraftBootstrap.getMissingDependenciesMap().entrySet()) {
                textY += 10;
                drawCenteredString(fontRendererObj, ChatColor.RED + entry.getKey().getName() + " needs " + StringUtils
                    .join(entry.getValue(), ", ") + " to load.", width / 2, textY, Color.WHITE.getRGB());
            }
        }

        textY += 10;
        if (!AddonMinecraftBootstrap.getDependenciesLoopMap().isEmpty()) {
            for (Map.Entry<AddonManifest, ArrayList<AddonManifest>> entry : AddonMinecraftBootstrap.getDependenciesLoopMap().entrySet()) {
                textY += 10;
                drawCenteredString(fontRendererObj, ChatColor.RED + entry.getKey().getName() + " can't load together with " +
                    StringUtils.join(entry.getValue().stream().map(AddonManifest::getName).collect(Collectors
                    .toList()), ", " + "."), width / 2, textY, Color.WHITE.getRGB());
            }
        }

        int hoverColor = new Color(0, 0, 0, 60).getRGB();
        int color = new Color(0, 0, 0, 50).getRGB();
        GuiBlock block1 = new GuiBlock(width / 2 - 100, width / 2 + 100, 200, 220);
        Gui.drawRect(block1.getLeft(), block1.getTop(), block1.getRight(), block1.getBottom(), block1.isMouseOver(mouseX, mouseY) ? hoverColor : color);
        drawCenteredString(fontRendererObj, "Continue", width / 2, 205, Color.WHITE.getRGB());

        if (block1.isMouseOver(mouseX, mouseY) && Mouse.isButtonDown(0)) {
            // Clear the maps so this screen goes away.
            AddonMinecraftBootstrap.getDependenciesLoopMap().clear();
            AddonMinecraftBootstrap.getMissingDependenciesMap().clear();
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiHyperiumScreenMainMenu());
        }
    }
}
