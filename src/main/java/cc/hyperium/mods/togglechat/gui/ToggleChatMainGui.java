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

package cc.hyperium.mods.togglechat.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.togglechat.ToggleChatMod;
import cc.hyperium.mods.togglechat.toggles.ToggleBase;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.Color;
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

    private boolean changed = false;

    private int pageNumber;

    public ToggleChatMainGui(ToggleChatMod main, int pageNumber) {
        this.pageNumber = pageNumber;

        this.main = main;

        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        setupPage();
    }

    private void setupPage() {
        if (this.main.getToggleHandler().getToggles().values().size() > 0) {

            int pages = (int) Math.ceil((double) this.main.getToggleHandler().getToggles().size() / 7D);

            if (this.pageNumber < 1 || this.pageNumber > pages) {
                this.pageNumber = 1;
            }

            final int[] position = {this.height / 2 - 75};

            this.main.getToggleHandler().getToggles().values().stream().skip((this.pageNumber - 1) * 7).limit(7).forEach(baseType -> {
                GuiButton button = new GuiButton(0, this.width / 2 - 75, position[0], 150, 20, String.format(baseType.getDisplayName(), baseType.getStatus(baseType.isEnabled())));

                this.data.put(button, baseType);
                this.buttonList.add(button);

                position[0] += 24;
            });

//            GuiButton last = new GuiButton(1, this.width - 114, this.height - 25, 50, 20, "\u21E6");
//            GuiButton next = new GuiButton(2, this.width - 60, this.height - 25, 50, 20, "\u21E8");


            GuiButton last = new GuiButton(1, this.width / 2 - 51, this.height / 2 + 90, 50, 20, "\u21E6");
            GuiButton next = new GuiButton(2, this.width / 2 + 1, this.height / 2 + 90, 50, 20, "\u21E8");


            this.buttonList.add(last);
            this.buttonList.add(next);

            last.enabled = this.pageNumber > 1;
            next.enabled = this.pageNumber != pages;
        }
    }

    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();

        drawCenteredString(this.fontRendererObj, String.format("Page %s/%s", (this.pageNumber), (int) Math.ceil((double) this.main.getToggleHandler().getToggles().size() / 7D)), this.width / 2, this.height / 2 - 94, Color.WHITE.getRGB());

        super.drawScreen(x, y, ticks);

        checkHover(this.height / 2 - 75);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                this.mc.displayGuiScreen(new ToggleChatMainGui(this.main, this.pageNumber - 1));
                return;
            case 2:
                this.mc.displayGuiScreen(new ToggleChatMainGui(this.main, this.pageNumber + 1));
                return;
        }

        // Make sure the id is 0 to prevent other buttons being pressed
        if (button.id == 0) {
            for (ToggleBase base : this.main.getToggleHandler().getToggles().values()) {
                if (this.data.containsKey(button) && base.equals(this.data.get(button))) {
                    base.toggle();
                    button.displayString = (String.format(base.getDisplayName(), base.getStatus(base.isEnabled())));
                    this.changed = true;
                    break;
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        if (this.changed) {
            this.main.getConfigLoader().saveToggles();
        }

        // Prevent memory leaks
        this.data.clear();
        this.buttonList.clear();
    }

    private void checkHover(int firstPosition) {
        if (this.buttonList.isEmpty()) {
            return;
        }

        for (GuiButton button : this.buttonList) {
            if (button == null || !this.data.containsKey(button)) continue;

            if (button.isMouseOver()) {
                ToggleBase toggleBase = this.data.get(button);

                if (!toggleBase.hasDescription()) continue;

                final int[] position = {firstPosition};
                toggleBase.getDescription().forEach(text -> {
                    drawCenteredString(this.mc.fontRendererObj, ChatColor.translateAlternateColorCodes('&', text), this.width / 2 + 150, position[0], Color.WHITE.getRGB());
                    position[0] += 10;
                });
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
