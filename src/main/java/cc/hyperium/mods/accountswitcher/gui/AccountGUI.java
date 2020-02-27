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

package cc.hyperium.mods.accountswitcher.gui;

import cc.hyperium.Hyperium;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class AccountGUI extends GuiScreen {

    private final GuiScreen prevGui;
    private GuiTextField usernameField, passwordField;

    public AccountGUI(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        // add them fields
        passwordField = new GuiTextField(0, mc.fontRendererObj, width / 4, height / 2 + 20, width / 2, 20);
        passwordField.setVisible(true);
        usernameField = new GuiTextField(0, mc.fontRendererObj, width / 4, height / 2 - 20, width / 2, 20);
        usernameField.setVisible(true);

        // add them buttons
        buttonList.add(new GuiButton(1, width / 2 - 150 / 2, height / 2 + 50, 150, 20,
                I18n.format("button.accountswitch.login")));
        buttonList.add(new GuiButton(2, width / 2 - 150 / 2, height / 2 + 72, 150, 20,
                I18n.format("gui.cancel")));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        passwordField.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        // draw the text boxes, so that they show on the gui
        usernameField.drawTextBox();
        passwordField.drawTextBox();
        // draw strings indicating which box is which
        drawCenteredString(mc.fontRendererObj, "Username", width / 4 + 20, height / 2 - 35, 0xffffff);
        drawCenteredString(mc.fontRendererObj, "Password", width / 4 + 20, height / 2 + 8, 0xffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        // update the cursor on the focused box
        if (usernameField.isFocused())
            usernameField.updateCursorCounter();
        if (passwordField.isFocused())
            passwordField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        // type a character into the focused box
        if (usernameField.isFocused())
            usernameField.textboxKeyTyped(typedChar, keyCode);
        if (passwordField.isFocused())
            passwordField.textboxKeyTyped(typedChar, keyCode);

        // if the user wants to escape the gui
        if (keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(prevGui);

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                // make sure either field isn't empty
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) return;
                // try and login
                Hyperium.INSTANCE.getModIntegration().getAccountSwitcher().getAccountManager().setUser(usernameField.getText(), passwordField.getText());
                break;
            case 2:
                mc.displayGuiScreen(prevGui);
                break;
        }

        super.actionPerformed(button);
    }

    public void display() {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(this);
    }
}
