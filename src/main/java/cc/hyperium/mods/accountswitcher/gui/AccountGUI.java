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
  private GuiTextField currentlyFocusedField;

  public AccountGUI(GuiScreen prevGui) {
    this.prevGui = prevGui;
  }

  @Override
  public void initGui() {
    Keyboard.enableRepeatEvents(true);
    buttonList.clear();
    // add them fields
    passwordField = new GuiTextField(0, mc.fontRendererObj, width >> 2, (height >> 1) + 20,
      width >> 1, 20);
    passwordField.setVisible(true);
    usernameField = new GuiTextField(1, mc.fontRendererObj, width >> 2, (height >> 1) - 20,
      width >> 1, 20);
    usernameField.setVisible(true);
    usernameField.setFocused(true);
    currentlyFocusedField = usernameField;

    // add them buttons
    buttonList.add(new GuiButton(2, (width >> 1) - 150 / 2, (height >> 1) + 50, 150, 20,
      I18n.format("button.accountswitch.login")));
    buttonList.add(new GuiButton(3, (width >> 1) - 150 / 2, (height >> 1) + 72, 150, 20,
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
    drawCenteredString(mc.fontRendererObj, "Username", (width >> 2) + 24, (height >> 1) - 35,
      0xffffff);
    drawCenteredString(mc.fontRendererObj, "Password", (width >> 2) + 24, (height >> 1) + 8,
      0xffffff);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  public void updateScreen() {
    // update the cursor on the focused box
    usernameField.updateCursorCounter();
    passwordField.updateCursorCounter();
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    // type a character into the focused box
    usernameField.textboxKeyTyped(typedChar, keyCode);
    passwordField.textboxKeyTyped(typedChar, keyCode);
    super.keyTyped(typedChar, keyCode);

    // if the user wants to escape the gui
    if (keyCode == Keyboard.KEY_ESCAPE) {
      mc.displayGuiScreen(prevGui);
    }

    if (keyCode == Keyboard.KEY_TAB) {
      if (currentlyFocusedField == usernameField) {
        currentlyFocusedField = passwordField;
        usernameField.setFocused(false);
        passwordField.setFocused(true);
      } else if (currentlyFocusedField == passwordField) {
        currentlyFocusedField = usernameField;
        passwordField.setFocused(false);
        usernameField.setFocused(true);
      }
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    switch (button.id) {
      case 2:
        // make sure either field isn't empty
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
          System.out.println("ahh");
          return;
        }
        // try and login
        Hyperium.INSTANCE.getModIntegration().getAccountSwitcher().getAccountManager()
          .setUser(usernameField.getText(), passwordField.getText());
        break;
      case 3:
        mc.displayGuiScreen(prevGui);
        break;
    }
  }

  @Override
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }
}
