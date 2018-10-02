package cc.hyperium.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import javafx.stage.FileChooser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;

public class ChangeBackgroundGui extends GuiScreen {

    private final GuiScreen prevGui;

    private GuiTextField downloadUrlField;
    private String statusText = "Enter a URL below to change the background or choose file.";

    public ChangeBackgroundGui(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void initGui() {
        this.downloadUrlField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, width / 4, height / 2 - 10, width / 2, 20);
        this.downloadUrlField.setFocused(true);
        this.downloadUrlField.setMaxStringLength(150);
        this.buttonList.add(new GuiButton(1, width / 2 - 150 / 2, height / 2 + 20, 150, 15, "Set URL"));
        this.buttonList.add(new GuiButton(2, width / 2 - 150 / 2, height / 2 + 40, 150, 15, "Choose File"));
        this.buttonList.add(new GuiButton(3, width / 2 - 150 / 2, height / 2 + 60, 150, 15, "Cancel"));
    }

    @Override
    public void updateScreen() {
        this.downloadUrlField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.downloadUrlField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
            Minecraft.getMinecraft().displayGuiScreen(this.prevGui);
        if (keyCode == Keyboard.KEY_RETURN)
            handleDownload();
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1)
            handleDownload();
        if (button.id == 2)
            handleChooseFile();
        if (button.id == 3)
            Minecraft.getMinecraft().displayGuiScreen(prevGui);
        super.actionPerformed(button);
    }

    private void handleChooseFile(){
        FileDialog dialog = new FileDialog((Frame)null,"Select background image",FileDialog.LOAD);
        dialog.setFile("*.jpg;*.jpeg;*.png");
        dialog.setVisible(true);
        if(dialog.getFiles().length != 0) {
            String filename = dialog.getFiles()[0].getAbsolutePath();
            if (!filename.isEmpty()) {
                statusText = "Working...";
                InputStream input = null;
                OutputStream output = null;
                try {
                    input = new FileInputStream(filename);
                    output = new FileOutputStream(new File(Minecraft.getMinecraft().mcDataDir, "customImage.png"));
                    IOUtils.copy(input, output);
                    Settings.BACKGROUND = "CUSTOM";
                    statusText = "Done!";
                    Minecraft.getMinecraft().displayGuiScreen(prevGui);
                } catch (FileNotFoundException e) {
                    statusText = "Invalid path";
                } catch (IOException e) {
                    e.printStackTrace();
                    statusText = "Unknown error";
                }
            }
        }
    }

    private void handleDownload() {
        String text = this.downloadUrlField.getText().toLowerCase().trim();

        if (text.isEmpty()) {
            this.statusText = "The URL cannot be empty.";
            return;
        }

        if (text.startsWith("https")) {
            text = text.replaceFirst("https", "http");
        }

        if (!(text.endsWith(".png") || text.endsWith(".jpg") || text.endsWith(".jpeg")) || !text.startsWith("http")) {
            this.statusText = "Invalid PNG image url";
            return;
        }

        URL url;
        URLConnection con;
        DataInputStream dis;
        FileOutputStream fos;
        byte[] fileData;
        try {
            this.statusText = "Working...";
            url = new URL(downloadUrlField.getText());
            con = url.openConnection();
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            dis = new DataInputStream(con.getInputStream());
            fileData = new byte[con.getContentLength()];
            for (int q = 0; q < fileData.length; q++) {
                fileData[q] = dis.readByte();
            }
            dis.close();
            fos = new FileOutputStream(new File(Minecraft.getMinecraft().mcDataDir, "customImage.png"));
            fos.write(fileData);
            Settings.BACKGROUND = "CUSTOM";
            this.statusText = "Done!";
            Minecraft.getMinecraft().displayGuiScreen(prevGui);
            fos.close();
        } catch (Exception m) {
            this.statusText = "Error whilst downloading.";
            m.printStackTrace();
        }
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.downloadUrlField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        drawCenteredString(Minecraft.getMinecraft().fontRendererObj, this.statusText, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 50, 0xFFFFFF);
        drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "To make it show select \"custom\" in background settings.", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 30, 0xFFFFFF);
        this.downloadUrlField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
