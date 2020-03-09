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

package cc.hyperium.mods.togglechat.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.togglechat.ToggleChatMod;
import cc.hyperium.mods.togglechat.toggles.ToggleBase;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ToggleChatMainGui extends GuiScreen {

    //        - 99
    //        - 75
    //        - 51
    //        - 27
    //        - 3
    //        + 21
    //        + 45
    //        + 69

    private final ToggleChatMod main;
    private final Map<GuiButton, ToggleBase> data = new HashMap<>();

    private boolean changed;

    private int pageNumber;

    public ToggleChatMainGui(ToggleChatMod main, int pageNumber) {
        this.pageNumber = pageNumber;

        this.main = main;

        mc = Minecraft.getMinecraft();
    }

    @Override
    public void initGui() {
        buttonList.clear();

        setupPage();
    }

    private void setupPage() {
        if (main.getToggleHandler().getToggles().values().size() > 0) {

            int pages = (int) Math.ceil((double) main.getToggleHandler().getToggles().size() / 7D);
            if (pageNumber < 1 || pageNumber > pages) pageNumber = 1;

            final int[] position = {height / 2 - 75};

            long limit = 7;
            long toSkip = (pageNumber - 1) * 7;
            for (ToggleBase baseType : main.getToggleHandler().getToggles().values()) {
                if (toSkip > 0) {
                    toSkip--;
                    continue;
                }
                if (limit-- == 0) break;
                GuiButton button = new GuiButton(0, width / 2 - 75, position[0], 150, 20,
                        String.format(baseType.getDisplayName(), baseType.getStatus(baseType.isEnabled())));

                data.put(button, baseType);
                buttonList.add(button);
                position[0] += 24;
            }

            GuiButton last = new GuiButton(1, width / 2 - 51, height / 2 + 90, 50, 20, "<");
            GuiButton next = new GuiButton(2, width / 2 + 1, height / 2 + 90, 50, 20, ">");

            buttonList.add(last);
            buttonList.add(next);

            last.enabled = pageNumber > 1;
            next.enabled = pageNumber != pages;
        }
    }

    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();

        drawCenteredString(fontRendererObj, String.format("Page %s/%s", (pageNumber),
                (int) Math.ceil((double) main.getToggleHandler().getToggles().size() / 7D)), width / 2, height / 2 - 94, -1);
        super.drawScreen(x, y, ticks);

        checkHover(height / 2 - 75);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                mc.displayGuiScreen(new ToggleChatMainGui(main, pageNumber - 1));
                return;
            case 2:
                mc.displayGuiScreen(new ToggleChatMainGui(main, pageNumber + 1));
                return;
        }

        // Make sure the id is 0 to prevent other buttons being pressed
        if (button.id == 0) {
            for (ToggleBase base : main.getToggleHandler().getToggles().values()) {
                if (data.containsKey(button) && base.equals(data.get(button))) {
                    base.toggle();
                    button.displayString = (String.format(base.getDisplayName(), base.getStatus(base.isEnabled())));
                    changed = true;
                    break;
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        if (changed) main.getConfigLoader().saveToggles();

        // Prevent memory leaks
        data.clear();
        buttonList.clear();
    }

    private void checkHover(int firstPosition) {
        if (buttonList.isEmpty()) {
            return;
        }

        for (GuiButton button : buttonList) {
            if (button != null && data.containsKey(button)) {
                if (button.isMouseOver()) {
                    ToggleBase toggleBase = data.get(button);
                    if (toggleBase.hasDescription()) {
                        final int[] position = {firstPosition};
                        for (String text : toggleBase.getDescription()) {
                            drawCenteredString(mc.fontRendererObj, ChatColor.translateAlternateColorCodes('&', text), width / 2 + 150, position[0], Color.WHITE.getRGB());
                            position[0] += 10;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void display() {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(this);
    }


}
