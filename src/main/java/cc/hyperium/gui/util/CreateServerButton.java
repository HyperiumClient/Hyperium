package cc.hyperium.gui.util;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class CreateServerButton extends GuiScreen {

    private GuiScreen parent;

    public CreateServerButton(GuiScreen parent) {
        this.parent = parent;
    }

    private GuiTextField serverName;
    private GuiTextField serverIp;

    private GuiTextField currentlyFocusedField;

    @Override
    public void initGui() {
        serverName = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 2 - 28, 200, 20);
        serverName.setFocused(true);
        serverName.setMaxStringLength(32);
        serverName.setText(Settings.SERVER_BUTTON_NAME);

        serverIp = new GuiTextField(1, fontRendererObj, width / 2 - 100, height / 2 - 4, 200, 20);
        serverIp.setFocused(false);
        serverIp.setMaxStringLength(72);
        serverIp.setText(Settings.SERVER_IP);

        if (serverName.isFocused()) {
            currentlyFocusedField = serverName;
        } else if (serverIp.isFocused()) {
            currentlyFocusedField = serverIp;
        }

        buttonList.add(new GuiButton(2, width / 2 - 100, height / 2 + 20, I18n.format("gui.done")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        serverName.drawTextBox();
        serverIp.drawTextBox();
        if (serverName.getText().isEmpty()) {
            fontRendererObj.drawString(EnumChatFormatting.GRAY + I18n.format("gui.serverjoin.enterservername"), width / 2 - 96, height / 2 - 22, -1);
        }

        if (serverIp.getText().isEmpty()) {
            fontRendererObj.drawString(EnumChatFormatting.GRAY + I18n.format("gui.serverjoin.enterserverip"), width / 2 - 96, height / 2 + 2, -1);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 2) {
            mc.displayGuiScreen(parent);
        }

        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        serverName.textboxKeyTyped(typedChar, keyCode);
        serverIp.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
            mc.displayGuiScreen(parent);
        }

        if (keyCode == Keyboard.KEY_TAB) {
            if (currentlyFocusedField == serverIp) {
                currentlyFocusedField = serverName;
                serverIp.setFocused(false);
                serverName.setFocused(true);
            } else if (currentlyFocusedField == serverName) {
                currentlyFocusedField = serverIp;
                serverName.setFocused(false);
                serverIp.setFocused(true);
            }
        }
    }

    @Override
    public void updateScreen() {
        serverName.updateCursorCounter();
        serverIp.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        serverName.mouseClicked(mouseX, mouseY, mouseButton);
        serverIp.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        if (serverName.getText().isEmpty()) {
            serverName.setText("Join Hypixel");
        }

        if (serverIp.getText().isEmpty()) {
            serverIp.setText("mc.hypixel.net");
        }

        Settings.SERVER_BUTTON_NAME = serverName.getText();
        Settings.SERVER_IP = serverIp.getText();
        Hyperium.CONFIG.save();
        super.onGuiClosed();
    }
}
