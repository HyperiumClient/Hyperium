package cc.hyperium.gui;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class ChangeBackgroundGui extends GuiScreen {

    private final GuiScreen prevGui;
    private GuiTextField downloadUrlField;
    private String statusText = I18n.format("gui.changebackground.line1");

    ChangeBackgroundGui(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void initGui() {
        downloadUrlField = new GuiTextField(0, mc.fontRendererObj, width / 4, height / 2 - 10, width / 2, 20);
        downloadUrlField.setFocused(true);
        downloadUrlField.setMaxStringLength(150);
        buttonList.add(new GuiButton(1, width / 2 - 150 / 2, height / 2 + 20, 150, 20,
            I18n.format("button.changebackground.seturl")));
        buttonList.add(new GuiButton(2, width / 2 - 150 / 2, height / 2 + 42, 150, 20,
            I18n.format("button.changebackground.choosefile")));
        buttonList.add(new GuiButton(3, width / 2 - 150 / 2, height / 2 + 64, 150, 20,
            I18n.format("button.changebackground.resetbackground")));
        buttonList.add(new GuiButton(4, width / 2 - 150 / 2, height / 2 + 86, 150, 20,
            I18n.format("gui.cancel")));

        if (Minecraft.getMinecraft().isFullScreen()) Minecraft.getMinecraft().toggleFullscreen();
    }

    @Override
    public void updateScreen() {
        downloadUrlField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        downloadUrlField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(prevGui);
            if (Minecraft.getMinecraft().isFullScreen()) Minecraft.getMinecraft().toggleFullscreen();
        }

        if (keyCode == Keyboard.KEY_RETURN) handleDownload();
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                handleDownload();
                break;

            case 2:
                handleChooseFile();
                break;

            case 3:
                handleResetBackground();
                break;

            case 4:
                mc.displayGuiScreen(prevGui);
                if (Minecraft.getMinecraft().isFullScreen()) Minecraft.getMinecraft().toggleFullscreen();
                break;
        }

        super.actionPerformed(button);
    }

    private void handleResetBackground() {
        statusText = I18n.format("gui.changebackground.working");
        File file = new File(Minecraft.getMinecraft().mcDataDir, "customImage.png");
        if (file.exists()) file.delete();
        Settings.BACKGROUND = "4";
        statusText = I18n.format("gui.changebackground.done");
        mc.displayGuiScreen(prevGui);
    }

    private void handleChooseFile() {
        FileDialog dialog = new FileDialog((Frame) null, I18n.format("gui.changebackground.selectimage"), FileDialog.LOAD);
        dialog.setFile("*.jpg;*.jpeg;*.png");
        dialog.setVisible(true);

        if (dialog.getFiles().length != 0) {
            String fileName = dialog.getFiles()[0].getAbsolutePath();

            if (!fileName.isEmpty()) {
                statusText = I18n.format("gui.changebackground.working");
                InputStream inputStream;
                OutputStream outputStream;

                try {
                    inputStream = new FileInputStream(fileName);
                    outputStream = new FileOutputStream(new File(mc.mcDataDir, "customImage.png"));
                    IOUtils.copy(inputStream, outputStream);
                    Settings.BACKGROUND = "CUSTOM";
                    statusText = I18n.format("gui.changebackground.done");
                    inputStream.close();
                    outputStream.close();
                    mc.displayGuiScreen(prevGui);
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
        String text = downloadUrlField.getText().toLowerCase().trim();

        if (text.isEmpty()) {
            statusText = I18n.format("gui.changebackground.urlempty");
            return;
        }

        if (text.startsWith("https")) {
            text = text.replaceFirst("https", "http");
        }

        if (!(text.endsWith(".png") || text.endsWith(".jpg") || text.endsWith(".jpeg") || !text.startsWith("http"))) {
            statusText = I18n.format("gui.changebackground.invalidurl");
            return;
        }

        URL url;
        URLConnection connection;
        DataInputStream dataInputStream;
        FileOutputStream fileOutputStream;
        byte[] fileData;

        try {
            statusText = I18n.format("gui.changebackground.working");
            url = new URL(downloadUrlField.getText());
            connection = url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            dataInputStream = new DataInputStream(connection.getInputStream());
            fileData = new byte[connection.getContentLength()];

            for (int q = 0; q < fileData.length; q++) {
                fileData[q] = dataInputStream.readByte();
            }

            dataInputStream.close();
            fileOutputStream = new FileOutputStream(new File(mc.mcDataDir, "customImage.png"));
            fileOutputStream.write(fileData);
            Settings.BACKGROUND = "CUSTOM";
            statusText = I18n.format("gui.changebackground.done");
            dataInputStream.close();
            fileOutputStream.close();
            mc.displayGuiScreen(prevGui);
        } catch (Exception e) {
            statusText = I18n.format("gui.changebackground.downloaderror");
            e.printStackTrace();
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
        ScaledResolution resolution = new ScaledResolution(mc);
        drawCenteredString(mc.fontRendererObj, statusText, resolution.getScaledWidth() / 2, resolution.getScaledHeight() / 2 - 50, -1);
        drawCenteredString(mc.fontRendererObj, I18n.format("gui.changebackground.line2"),
            resolution.getScaledWidth() / 2, resolution.getScaledHeight() / 2 - 30, -1);
        downloadUrlField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
