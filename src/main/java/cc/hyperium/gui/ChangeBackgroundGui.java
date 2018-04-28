package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ChangeBackgroundGui extends GuiScreen {

    private GuiScreen prevGui;

    private GuiTextField downloadUrlField;
    private String statusText = "Enter a URL below to change the background.";

    public ChangeBackgroundGui(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        downloadUrlField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, sr.getScaledWidth() / 4, sr.getScaledHeight() / 2 - 10, sr.getScaledWidth() / 2, 20);
        downloadUrlField.setFocused(true);
        downloadUrlField.setMaxStringLength(150);
        buttonList.add(new GuiButton(1, sr.getScaledWidth() / 2 - 150 / 2, sr.getScaledHeight() / 2 + 20, 150, 15, "Set"));
        buttonList.add(new GuiButton(2, sr.getScaledWidth() / 2 - 150 / 2, sr.getScaledHeight() / 2 + 40, 150, 15, "Cancel"));
        super.initGui();
    }

    @Override
    public void updateScreen() {
        downloadUrlField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        downloadUrlField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
            Minecraft.getMinecraft().displayGuiScreen(prevGui);
        if (keyCode == Keyboard.KEY_RETURN)
            handleDownload();
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1)
            handleDownload();
        if (button.id == 2)
            Minecraft.getMinecraft().displayGuiScreen(prevGui);
        super.actionPerformed(button);
    }

    private void handleDownload() {
        if (downloadUrlField.getText().equalsIgnoreCase("")) {
            statusText = "The URL cannot be empty.";
            return;
        }
        if (!downloadUrlField.getText().endsWith(".png")) {
            statusText = "That is not a png file.";
            return;
        }
        URL url;
        URLConnection con;
        DataInputStream dis;
        FileOutputStream fos;
        byte[] fileData;
        try {
            statusText = "Working...";
            url = new URL(downloadUrlField.getText());
            con = url.openConnection();
            dis = new DataInputStream(con.getInputStream());
            fileData = new byte[con.getContentLength()];
            for (int q = 0; q < fileData.length; q++) {
                fileData[q] = dis.readByte();
            }
            dis.close();
            fos = new FileOutputStream(new File(Minecraft.getMinecraft().mcDataDir, "customImage.png"));
            fos.write(fileData);
            statusText = "Done!";
            Minecraft.getMinecraft().displayGuiScreen(prevGui);
            fos.close();
        }
        catch(Exception m) {
            statusText = "Error whilst downloading.";
            System.out.println(m);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        downloadUrlField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        drawCenteredString(Minecraft.getMinecraft().fontRendererObj, statusText, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 50, 0xFFFFFF);
        drawCenteredString(Minecraft.getMinecraft().fontRendererObj,"To make it show select \"custom\" in background settings.", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 30, 0xFFFFFF);
        downloadUrlField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
