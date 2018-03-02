/*
 * Hyperium Client, Free client with huds and popular mod
 * Copyright (C) 2018  Hyperium Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package cc.hyperium.gui;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeyBindPressEvent;
import cc.hyperium.gui.settings.items.NameHistorySettings;
import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import cc.hyperium.utils.HyperiumFontRenderer;
import me.kbrewster.mojangapi.MojangAPI;
import me.kbrewster.mojangapi.profile.Name;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NameHistoryGui extends HyperiumGui {

    List<String> names = new ArrayList<>();
    private HyperiumFontRenderer fontRenderer = new HyperiumFontRenderer("Arial", Font.PLAIN, 16);
    private HyperiumFontRenderer smallRenderer = new HyperiumFontRenderer("Arial", Font.PLAIN, 14);
    private GuiTextField nameField;

    @Override
    public void initGui() {
        super.initGui();
        nameField = new GuiTextField(1, Minecraft.getMinecraft().fontRendererObj, width / 2 - (115 / 2), height / 5 + 10, 115, 20);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //BG
        drawRect(width / 5 - 1, height / 5 - 1, width - width / 5, height / 5 + 31 + (names.size() * 11), new Color(0, 0, 0, 100).getRGB());

        //TITLE BG
        drawRect(width / 5 - 1, height / 5 - 1, width - width / 5, height / 5 + 32, new Color(0, 0, 0, 150).getRGB());

        //TITLE
        fontRenderer.drawCenteredStringWithShadow("NAME HISTORY", width / 2, height / 5, Color.WHITE.getRGB());

        //Text Box
        nameField.drawTextBox();
        int defaultColour = Color.WHITE.getRGB();
        if (NameHistorySettings.rgbNamesEnabled) {
            defaultColour = Color.getHSBColor(System.currentTimeMillis() % 1000L / 1000F, 1F, 1F).getRGB();
        }
        for (int i = 0; i < names.size(); i++) {
            Minecraft.getMinecraft().fontRendererObj.drawString(names.get(i), width / 2 - (115 / 2), height / 5 + 30 + 5 + (i * 10), defaultColour);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void pack() {

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_RETURN) {
            names.clear();
            getNames(nameField.getText());
        }
        nameField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        nameField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
        System.out.println("NAME HISTORY: Mouse Clicked");
    }

    @InvokeEvent
    public void onKeyBindPress(KeyBindPressEvent event) {
        if (event.getKeyCode() == KeyBindHandler.nameHistory.getKey()) {
            new NameHistoryGui().show();
        }
    }

    public void getNames(String username) {
        try {
            UUID uuid = MojangAPI.getUUID(username);
            for (Name history : MojangAPI.getNameHistory(uuid)) {
                String name = history.getName();
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                if (history.getChangedToAt() == 0) {
                    names.add(name);
                } else {
                    names.add(String.format("%s -> %s", name, format.format(history.getChangedToAt())));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
