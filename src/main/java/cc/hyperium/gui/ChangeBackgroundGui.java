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

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import javafx.stage.FileChooser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
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
    private String statusText = I18n.format("gui.changebackground.line1");

    public ChangeBackgroundGui(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void initGui() {
        this.downloadUrlField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, width / 4, height / 2 - 10, width / 2, 20);
        this.downloadUrlField.setFocused(true);
        this.downloadUrlField.setMaxStringLength(150);
        this.buttonList.add(new GuiButton(1, width / 2 - 150 / 2, height / 2 + 20, 150, 15, I18n.format("button.changebackground.seturl")));
        this.buttonList.add(new GuiButton(2, width / 2 - 150 / 2, height / 2 + 40, 150, 15, I18n.format("button.changebackground.choosefile")));
        this.buttonList.add(new GuiButton(3, width / 2 - 150 / 2, height / 2 + 60, 150, 15, I18n.format("button.changebackground.resetbackground")));
        this.buttonList.add(new GuiButton(4, width / 2 - 150 / 2, height / 2 + 80, 150, 15, I18n.format("gui.cancel")));
        if(Minecraft.getMinecraft().isFullScreen()) {
            Minecraft.getMinecraft().toggleFullscreen();
        }
    }

    @Override
    public void updateScreen() {
        this.downloadUrlField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.downloadUrlField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(this.prevGui);
            if(Minecraft.getMinecraft().isFullScreen()) {
                Minecraft.getMinecraft().toggleFullscreen();
            }
        }
        if (keyCode == Keyboard.KEY_RETURN) {
            handleDownload();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            handleDownload();
        }
        if (button.id == 2) {
            handleChooseFile();
        }
        if (button.id == 3) {
            handleResetBackground();
        }
        if (button.id == 4) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(prevGui);
            if(Minecraft.getMinecraft().isFullScreen()) {
                Minecraft.getMinecraft().toggleFullscreen();
            }
        }
        super.actionPerformed(button);
    }

    private void handleResetBackground() {
        statusText = I18n.format("gui.changebackground.working");
        File file = new File(Minecraft.getMinecraft().mcDataDir, "customImage.png");
        if (file.exists()) {
            file.delete();
        }
        Settings.BACKGROUND = "4";
        statusText = I18n.format("gui.changebackground.done");
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(prevGui);
    }

    private void handleChooseFile() {
        FileDialog dialog = new FileDialog((Frame) null, I18n.format("gui.changebackground.selectimage"), FileDialog.LOAD);
        dialog.setFile("*.jpg;*.jpeg;*.png");
        dialog.setVisible(true);
        if (dialog.getFiles().length != 0) {
            String filename = dialog.getFiles()[0].getAbsolutePath();
            if (!filename.isEmpty()) {
                statusText = I18n.format("gui.changebackground.working");
                InputStream input = null;
                OutputStream output = null;
                try {
                    input = new FileInputStream(filename);
                    output = new FileOutputStream(new File(Minecraft.getMinecraft().mcDataDir, "customImage.png"));
                    IOUtils.copy(input, output);
                    Settings.BACKGROUND = "CUSTOM";
                    statusText = I18n.format("gui.changebackground.done");
                    Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(prevGui);
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
            this.statusText = I18n.format("gui.changebackground.urlempty");
            return;
        }

        if (text.startsWith("https")) {
            text = text.replaceFirst("https", "http");
        }

        if (!(text.endsWith(".png") || text.endsWith(".jpg") || text.endsWith(".jpeg")) || !text.startsWith("http")) {
            this.statusText = I18n.format("gui.changebackground.invalidurl");
            return;
        }

        URL url;
        URLConnection con;
        DataInputStream dis;
        FileOutputStream fos;
        byte[] fileData;
        try {
            this.statusText = I18n.format("gui.changebackground.working");
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
            this.statusText = I18n.format("gui.changebackground.done");
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(prevGui);
            fos.close();
        } catch (Exception m) {
            this.statusText = I18n.format("gui.changebackground.downloaderror");
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
        drawCenteredString(Minecraft.getMinecraft().fontRendererObj, I18n.format("gui.changebackground.line2"), sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 30, 0xFFFFFF);
        this.downloadUrlField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
