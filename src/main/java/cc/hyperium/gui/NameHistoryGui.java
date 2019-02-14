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

package cc.hyperium.gui;

import cc.hyperium.utils.HyperiumFontRenderer;
import me.kbrewster.mojangapi.MojangAPI;
import me.kbrewster.mojangapi.profile.Name;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NameHistoryGui extends GuiScreen {

    private String name;

    public NameHistoryGui() {
        this("");
    }

    public NameHistoryGui(String name) {
        this.name = name;
        getNames(name);
    }

    List<String> names = new ArrayList<>();
    private final HyperiumFontRenderer fontRenderer = new HyperiumFontRenderer("Arial", Font.PLAIN, 16);
    private GuiTextField nameField;
    private int offset = 0;

    @Override
    public void initGui() {
        super.initGui();
        nameField = new GuiTextField(1, mc.fontRendererObj, width / 2 - (115 / 2), height / 5 + 10, 115, 20);
        nameField.setText(name);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int left = width / 5 - 1;
        int top = height / 5 - 1;
        int right = width - width / 5;
        int bottom = height / 5 + 33;

        //BG
        drawRect(left, top, right, bottom + (names.size() * 10), new Color(0, 0, 0, 100).getRGB());

        //TITLE BG
        drawRect(left, top, right, bottom, new Color(0, 0, 0, 150).getRGB());

        //TITLE
        fontRenderer.drawCenteredString(I18n.format("gui.namehistory.text"), width / 2, height / 5, Color.WHITE.getRGB());

        //Text Box
        nameField.drawTextBox();
        int defaultColour = Color.WHITE.getRGB();
        for (int i = 0; i < names.size(); i++) {

            float xPos = width / 2 - (115 / 2);
            float yPos = bottom + (i * 10) + offset;

            // Check if names have been scrolled outside of bounding box.
            if (yPos < (height / 5) + 32) {
                continue;
            }

            // Highlight current and original names.
            if (i == 0) {
                fontRenderer.drawString(names.get(i), xPos, yPos, Color.YELLOW.getRGB());
            } else if (i == names.size() - 1) {
                fontRenderer.drawString(names.get(i), xPos, yPos, Color.GREEN.getRGB());
            } else {
                fontRenderer.drawString(names.get(i), xPos, yPos, defaultColour);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_RETURN) {
            names.clear();
            getNames(nameField.getText());
        }
        nameField.textboxKeyTyped(typedChar, keyCode);
        name = nameField.getText();
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        nameField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void getNames(String username) {
        offset = 0;
        try {
            if (username.isEmpty()) {
                return;
            }
            UUID uuid = MojangAPI.getUUID(username);
            for (Name history : MojangAPI.getNameHistory(uuid)) {
                String name = history.getName();
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                if (history.getChangedToAt() == 0) {
                    names.add(name);
                } else {
                    // Adds name and date changed to the array to be displayed.
                    names.add(String.format("%s -> %s", name, format.format(history.getChangedToAt())));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i < 0) {
            // works out length of scrollable area
            int length = height / 5 - (int) (names.size() * fontRenderer.getHeight("s"));

            if (offset - length + 1 > -names.size() && length <= names.size()) {
                // regions it cant exceed
                offset -= 1;
            }
        } else if (i > 0 && offset < 0) {
            offset += 1;
        }
    }
}
