/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui.settings;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.font.Fonts;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingGui extends HyperiumGui {
    protected List<SettingItem> settingItems = new ArrayList<>();
    private int offset = 0;
    private HyperiumFontRenderer fontRendererObj = Fonts.ARIAL.getTrueTypeFont();
    private String name;
    private GuiScreen previous;

    private int animation = (height / 5) * 3;

    public SettingGui(String name, GuiScreen previous){
        this.name = name;
        this.previous = previous;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        // The Box
        drawRect(width / 5, height / 5, width - width / 5, height - height / 5, new Color(0, 0, 0, 100).getRGB());
        // Top bar
        drawRect(width / 5, height / 5, width -width/5, height / 5  + 25, new Color(0, 0, 0, 30).getRGB());
        int items = (height - (getY() * 2 + 25)) / 15;

        settingItems.stream()
                .filter(i -> items - i.id >= offset && (getY() + 28) + (offset + i.id) * 15 >= getY() + 25)
                .forEach(i -> {
                    i.visible = true;
                    i.drawItem(mc, mouseX, mouseY, getX() + animation, (getY() + 25) + (offset + i.id) * 15);
                });
        fontRendererObj.drawString(">", (width - width / 5) - 18, (height /5) + 5, 0xFFFFFF);
        fontRendererObj.drawString(name, width / 5 + 10, (height / 5) + ((25 - 9) / 2), 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected int getY(){
        return height / 5;
    }

    protected int getX(){
        return width / 5;
    }

    @Override
    protected void pack() {
        settingItems.clear();
        reg("", new GuiButton(0, (width / 5), height /5, (width /5) * 3, 25, ""), b -> Minecraft.getMinecraft().displayGuiScreen(previous), b->{});
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i < 0)
            offset += 1;
        else if (i > 0)
            offset -= 1;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            for (SettingItem i : settingItems) {
                if (i.mousePressed(mc, mouseX, mouseY)) {
                    i.playPressSound(mc.getSoundHandler());
                    i.callback.accept(i);
                }
            }
        }
    }
}
