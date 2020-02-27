package cc.hyperium.mods.accountswitcher.gui;

import cc.hyperium.Hyperium;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class AddAccountGUI extends GuiScreen {

    private final GuiScreen prevGui;
    private GuiTextField usernameField;
    private GuiTextField passwordField;

    public AddAccountGUI(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        passwordField = new GuiTextField(0, mc.fontRendererObj, width / 4, height / 2 + 20, width / 2, 20);
        passwordField.setVisible(true);
        usernameField = new GuiTextField(0, mc.fontRendererObj, width / 4, height / 2 - 15, width / 2, 20);
        usernameField.setVisible(true);

        buttonList.add(new GuiButton(1, width / 2 - 150 / 2, height / 2 + 40, 150, 20,
                I18n.format("button.accountswitch.add")));
        buttonList.add(new GuiButton(2, width / 2 - 150 / 2, height / 2 + 62, 150, 20,
                I18n.format("gui.cancel")));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseY <= 160 || mouseY >= 140) {
            usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (mouseY <= 125 || mouseY >= 105) {
            passwordField.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        usernameField.drawTextBox();
        passwordField.drawTextBox();
        drawCenteredString(mc.fontRendererObj, "Username", width / 4 + 20, height / 2 - 45, 0xffffff);
        drawCenteredString(mc.fontRendererObj, "Password", width / 4 + 20, height / 2 - 5, 0xffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        if (usernameField.isFocused())
            usernameField.updateCursorCounter();
        if (passwordField.isFocused())
            passwordField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (usernameField.isFocused())
            usernameField.textboxKeyTyped(typedChar, keyCode);
        if (passwordField.isFocused())
            passwordField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(prevGui);

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) return;
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
