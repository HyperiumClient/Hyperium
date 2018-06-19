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

package cc.hyperium.mods.skinchanger.gui;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.skinchanger.SkinChangerMod;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.io.IOException;

public class SkinChangerGui extends GuiScreen {

    private final SkinChangerMod mod;

//    private FakePlayerUtils.FakePlayer fakePlayer = FakePlayerUtils.getFakePlayer();

    private GuiTextField textField;

    private String message = "";

    private boolean previewCape = false;

    public SkinChangerGui(SkinChangerMod mod) {
        this(mod, "");
    }

    public SkinChangerGui(SkinChangerMod mod, String message) {
        this.mod = mod;
        this.message = message;

//        this.mod.getSkinManager().updatePlayer(null);
//        this.fakePlayerSkinManager.replaceSkin(DefaultPlayerSkin.getDefaultSkinLegacy());
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        this.textField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 150, this.height / 2 - 22, 300, 20);

        this.buttonList.add(new GuiButton(1, this.width / 2 - 160, this.height / 2 + 26, 150, 20, "Preview skin"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 160, this.height / 2 + 50, 150, 20, "Reset skin"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 160, this.height / 2 + 74, 150, 20, "Confirm skin"));

        this.buttonList.add(new GuiButton(4, this.width / 2 + 10, this.height / 2 + 26, 150, 20, "Preview cape"));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 10, this.height / 2 + 50, 150, 20, "Reset cape"));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 10, this.height / 2 + 74, 150, 20, "Add cape"));

        this.textField.setMaxStringLength(16);
        this.textField.setText(this.message);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        this.fontRendererObj.drawString("Skin Settings", this.width / 2 - 118, this.height / 2 + 8, Color.WHITE.getRGB(), false);
        this.fontRendererObj.drawString("Cape Settings", this.width / 2 + 50, this.height / 2 + 8, Color.WHITE.getRGB(), false);

//        drawEntityOnScreen(this.width / 2, this.height / 2 - 45, 35, this.width / 2 - mouseX, (this.height / 2 - 90) - mouseY, this.fakePlayer, this.previewCape);

        if (this.previewCape) {
            drawCenteredString(this.mc.fontRendererObj, "Preview Cape", this.width / 2, this.height / 2 - 40, Color.WHITE.getRGB());
        } else {
            drawCenteredString(this.mc.fontRendererObj, "Preview Skin", this.width / 2, this.height / 2 - 40, Color.WHITE.getRGB());
        }

        if (this.previewCape) {
            drawCenteredString(this.mc.fontRendererObj, ChatColor.WHITE + "Hold Left-Alt to flip the cape!", this.width / 2, this.height / 2 + 100, Color.WHITE.getRGB());
        }

        this.textField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                this.previewCape = false;
//                this.fakePlayerCapeManager.removeCape();
//                if (!this.textField.getText().isEmpty() && this.textField.getText().length() >= 2) {
//                    this.fakePlayerSkinManager.update(this.textField.getText());
//                }
                break;
            case 2:
                this.mod.getConfig().setExperimental(false);
                this.mod.getConfig().setSkinName(Minecraft.getMinecraft().getSession().getUsername());
                sendChatMessage("Your skin has been reset!");
                this.mc.displayGuiScreen(null);
                break;
            case 3:
                if (!this.textField.getText().isEmpty() && this.textField.getText().length() >= 2) {
                    this.mod.getConfig().setExperimental(false);
                    this.mod.getConfig().setSkinName(this.textField.getText());
                    sendChatMessage(String.format("Your skin has been updated to %s!", ChatColor.GOLD + this.textField.getText() + ChatColor.GRAY));
                } else {
                    sendChatMessage("Not enough characters provided");
                    sendChatMessage("Use a name between 2 and 16 characters!");
                }
                this.mc.displayGuiScreen(null);
                break;
            case 4:
                this.previewCape = true;
//                this.fakePlayerCapeManager.addCape();
                break;
            case 5:
                this.mod.getConfig().setExperimental(false);
                this.mod.getConfig().setUsingCape(false);
                this.mod.getConfig().setOfCapeName("");
                sendChatMessage("Your cape has been removed!");
                this.mc.displayGuiScreen(null);
                break;
            case 6:
                this.mod.getConfig().setExperimental(false);
                this.mod.getConfig().setUsingCape(true);
                this.mod.getConfig().setOfCapeName(this.textField.getText());
                sendChatMessage("You now have a cape!");
                this.mc.displayGuiScreen(null);
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void sendChatMessage(String msg) {
        GeneralChatHandler.instance().sendMessage(ChatColor.RED + msg, false);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mod.getConfig().save();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void display() {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onTick(TickEvent event) {
        EventBus.INSTANCE.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }
}
