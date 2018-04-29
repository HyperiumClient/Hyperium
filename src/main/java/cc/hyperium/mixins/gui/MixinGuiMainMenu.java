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
import cc.hyperium.gui.GuiBlock;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    boolean overLast = false;

    private boolean clickedCheckBox = false;

    /**
     * Override initGui
     *
     * @author Cubxity
     */
    @Overwrite
    public void initGui() {
        if (Hyperium.INSTANCE.isAcceptedTos()) {
            Minecraft.getMinecraft().displayGuiScreen(new HyperiumMainMenu());
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
        this.drawDefaultBackground();
        if (!Hyperium.INSTANCE.isAcceptedTos()) {
            drawCenteredString(this.fontRendererObj, ChatColor.RED + "By continuing, you acknowledge this client is " + ChatColor.BOLD + "USE AT YOUR OWN RISK", width / 2, 90, Color.WHITE.getRGB());
            drawCenteredString(this.fontRendererObj, ChatColor.RED + "The developers of Hyperium are not responsible for any damages or bans ", width / 2, 100, Color.WHITE.getRGB());
            drawCenteredString(this.fontRendererObj, ChatColor.RED + "to your account while using this client", width / 2, 110, Color.WHITE.getRGB());
            drawCenteredString(this.fontRendererObj, ChatColor.RED + "By continuing you agree to the privacy policy (http://hyperium.cc/#privacy)", width / 2, 120, Color.WHITE.getRGB());

            //
            drawCenteredString(this.fontRendererObj, ChatColor.RED + "Please check the box to confirm you agree with these terms", width / 2, 130, Color.WHITE.getRGB());

            GuiBlock block = new GuiBlock(width / 2 - 10, width / 2 + 10, 145, 165);
            if (!overLast && Mouse.isButtonDown(0) && block.isMouseOver(mouseX, mouseY)) {
                clickedCheckBox = !clickedCheckBox;
            }

            RenderUtils.drawBorderedRect(block.getLeft(), block.getTop(), block.getRight(), block.getBottom(), 7, Color.BLACK.getRGB(), Color.RED.getRGB());
            if (clickedCheckBox) {
                RenderUtils.drawLine(block.getLeft(), block.getTop(), block.getRight(), block.getBottom(), 5, Color.BLACK.getRGB());
                RenderUtils.drawLine(block.getLeft(), block.getBottom(), block.getRight(), block.getTop(), 5, Color.BLACK.getRGB());
                int hoverColor = new Color(0, 0, 0, 60).getRGB();
                int color = new Color(0, 0, 0, 50).getRGB();
                GuiBlock block1 = new GuiBlock(width / 2 - 100, width / 2 + 100, 180, 200);
                Gui.drawRect(block1.getLeft(), block1.getTop(), block1.getRight(), block1.getBottom(), block1.isMouseOver(mouseX, mouseY) ? hoverColor : color);

                if (block1.isMouseOver(mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    Hyperium.INSTANCE.acceptTos();
                }
                drawCenteredString(fontRendererObj, ChatColor.RED + "Accept", width / 2, 185, Color.WHITE.getRGB());
            }
            overLast = Mouse.isButtonDown(0);
        } else if (!AddonMinecraftBootstrap.getDependenciesLoopMap().isEmpty() || !AddonMinecraftBootstrap.getMissingDependenciesMap().isEmpty()) {
            int textY = 20;
            drawCenteredString(this.fontRendererObj, ChatColor.RED.toString() + ChatColor.BOLD + "ERROR LOADING ADDONS", width / 2, textY, Color.WHITE.getRGB());
            textY += 10;
            drawCenteredString(this.fontRendererObj, ChatColor.RED.toString() + ChatColor.BOLD + "THE FOLLOWING ADDONS WON'T LOAD", width / 2, textY, Color.WHITE.getRGB());
            textY += 10;
            if (!AddonMinecraftBootstrap.getMissingDependenciesMap().isEmpty()) {
                for (Map.Entry<AddonManifest, ArrayList<String>> entry : AddonMinecraftBootstrap.getMissingDependenciesMap().entrySet()) {
                    textY += 10;
                    drawCenteredString(this.fontRendererObj, ChatColor.RED + entry.getKey().getName() + " needs " + StringUtils.join(entry.getValue(), ", ") + " to load.", width / 2, textY, Color.WHITE.getRGB());
                }
            }

            textY += 10;
            if (!AddonMinecraftBootstrap.getDependenciesLoopMap().isEmpty()) {
                for (Map.Entry<AddonManifest, ArrayList<AddonManifest>> entry : AddonMinecraftBootstrap.getDependenciesLoopMap().entrySet()) {
                    textY += 10;
                    drawCenteredString(this.fontRendererObj, ChatColor.RED + entry.getKey().getName() + " can't load together with " + StringUtils.join(entry.getValue().stream().map(AddonManifest::getName).collect(Collectors.toList()), ", " + "."), width / 2, textY, Color.WHITE.getRGB());
                }
            }

            int hoverColor = new Color(0, 0, 0, 60).getRGB();
            int color = new Color(0, 0, 0, 50).getRGB();
            GuiBlock block1 = new GuiBlock(width / 2 - 100, width / 2 + 100, 200, 220);
            Gui.drawRect(block1.getLeft(), block1.getTop(), block1.getRight(), block1.getBottom(), block1.isMouseOver(mouseX, mouseY) ? hoverColor : color);

            if (block1.isMouseOver(mouseX, mouseY) && Mouse.isButtonDown(0)) {
                // Clear the maps so this screen goes away.
                AddonMinecraftBootstrap.getDependenciesLoopMap().clear();
                AddonMinecraftBootstrap.getMissingDependenciesMap().clear();
            }
            drawCenteredString(fontRendererObj, "Continue", width / 2, 205, Color.WHITE.getRGB());
        } else {
            Minecraft.getMinecraft().displayGuiScreen(new HyperiumMainMenu());
        }
    }
}