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

import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.HyperiumFontRenderer;
import me.kbrewster.mojangapi.MojangAPI;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
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

    private List<String> names = new ArrayList<>();
    private final HyperiumFontRenderer fontRenderer = new HyperiumFontRenderer("Arial", Font.PLAIN, 16);
    private GuiTextField nameField;
    private int offset;

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

        //TITLE;
        drawCenteredString(mc.fontRendererObj, I18n.format("gui.namehistory.text"), width / 2, height / 5, Color.WHITE.getRGB());

        //Text Box
        nameField.drawTextBox();
        int defaultColour = Color.WHITE.getRGB();
        // Check if names have been scrolled outside of bounding box.
        // Highlight current and original names.
        int bound = names.size();
        for (int i = 0; i < bound; i++) {
            float xPos = (width >> 1) - (115 >> 1);
            float yPos = bottom + (i * 10) + offset;
            if (yPos < (height / 5f) + 32) continue;
            mc.fontRendererObj.drawString(names.get(i), (int) xPos, (int) yPos, i == 0 ? Color.YELLOW.getRGB() : i == names.size() - 1 ? Color.GREEN.getRGB() : defaultColour);
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

    @Override
    public void onGuiClosed() {
        names.clear();
        super.onGuiClosed();
    }

    private void getNames(String username) {
        offset = 0;
        try {
            if (username.isEmpty()) return;

            UUID uuid = MojangAPI.getUUID(username);

            Multithreading.runAsync(() -> MojangAPI.getNameHistory(uuid).forEach(history -> {
                String name = history.getName();
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                names.add(history.getChangedToAt() == 0 ? name : String.format("%s > %s", name, format.format(history.getChangedToAt())));
            }));
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
                offset -= 10;
            }
        } else if (i > 0 && offset < 0) {
            offset += 10;
        }
    }
}
