package cc.hyperium.gui.hyperium.tabs;

import cc.hyperium.Metadata;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.installer.api.entities.VersionManifest;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.DownloadTask;
import cc.hyperium.utils.InstallerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

/*
 * Created by Cubxity on 25/09/2018
 */
public class UpdateTab extends AbstractTab {
    private VersionManifest latest;
    private boolean buttonState;
    private DownloadTask dl;

    public UpdateTab(HyperiumMainGui gui) {
        super(gui, "Updater");
        Multithreading.runAsync(() -> latest = InstallerUtils.getManifest().getVersions()[0]);
    }

    @Override
    public void render(int x, int y, int width, int height) {
        boolean ua = latest != null && latest.getId() > Metadata.getVersionID();
        gui.getTitle().drawString(latest == null ? "Loading..." : ua ? "Hyperium " + latest.getName() : "No updates available", x + 10, y + 10, 0xffffff);
        if (ua) {
            gui.getFont().drawString("Changelog coming soon", x + 10, y + 25, 0x969696);
            Gui.drawRect(x + 10, y + height - 60, x + 160, y + height - 10, 0x70000000);
            ScaledResolution sr = ResolutionUtil.current();
            int sw = sr.getScaledWidth();
            int sh = sr.getScaledHeight();
            final int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;           // Mouse X
            final int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1; // Mouse Y
            if (mx >= x + 10 && mx <= x + 160 && my >= y + height - 60 && my <= y + height - 10 && Mouse.isButtonDown(0)) {
                if (!buttonState) {
                    buttonState = true;
                    //if (dl == null)
                    try {
                        File tmp = Files.createTempDirectory("hyperiumUpdate").toFile();
                        tmp.deleteOnExit();
                        dl = new DownloadTask(latest.getUrl(), tmp.getAbsolutePath());
                        Multithreading.runAsync(() -> {
                            dl.execute();
                            try {
                                dl.get();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                                dl = null;
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        dl = null;
                    }
                }
            } else if (buttonState)
                buttonState = false;
            if (dl == null)
                gui.getTitle().drawCenteredString("Update", x + 85, y + height - 45, 0xffffff);
            else {
                gui.getTitle().drawCenteredString(dl.getProgress() == 100 ? "Updating..." : "Downloading...", x + 85, y + height - 45, 0x969696);
                if (dl.getProgress() != 100) {
                    gui.getFont().drawCenteredString(dl.getProgress() + "%", x + 85, y + height - 30, 0x969696);
                    Gui.drawRect(0, gui.height - 2, gui.width / 100 * dl.getProgress(), gui.height, 0xff95c990);
                }
            }
        }
    }
}
