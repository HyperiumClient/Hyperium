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

package cc.hyperium.gui.hyperium.tabs;

import cc.hyperium.Hyperium;
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
import net.minecraft.client.resources.I18n;
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
    private String installState = "";
    private DownloadTask dl;

    public UpdateTab(HyperiumMainGui gui) {
        super(gui, "tab.update.name");
        Multithreading.runAsync(() ->
            latest = InstallerUtils.getManifest().getLatest());
    }

    @Override
    public void render(int x, int y, int width, int height) {
        boolean ua = latest != null && latest.getId() > Metadata.getVersionID();
        String text = latest == null ? "Loading..." : ua ? "Hyperium " + (latest.getName() + (latest.getBeta() ? " (" + latest.getId() + ")" : "")) : I18n.format("tab.update.noupdates");
        gui.getTitle().drawString(text, x + 10, y + 10, 0xffffff);
        if (ua) {
            gui.getFont().drawString(dl != null && dl.getProgress() == 100 ? I18n.format("tab.update.autorestart") : I18n.format("tab.update.changelog"), x + 10, y + 25, 0x969696);
            Gui.drawRect(x + 10, y + height - 60, x + 160, y + height - 10, 0x70000000);
            ScaledResolution sr = ResolutionUtil.current();
            int sw = sr.getScaledWidth();
            int sh = sr.getScaledHeight();
            buttonState = false;
            final int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;           // Mouse X
            final int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1; // Mouse Y
            if (mx >= x + 10 && mx <= x + 160 && my >= y + height - 60 && my <= y + height - 10 && Mouse.isButtonDown(0)) {
                if (!buttonState) {
                    buttonState = true;
                    if (dl == null)
                        try {
                            File tmp = Files.createTempDirectory("hyperiumUpdate").toFile();
                            tmp.deleteOnExit();
                            dl = new DownloadTask(latest.getUrl(), tmp.getAbsolutePath());
                            Multithreading.runAsync(() -> {
                                dl.execute();
                                try {
                                    dl.get();
                                    installState = I18n.format("tab.update.installer.state.loading");
                                    try {
                                        installState = I18n.format("tab.update.installer.state.restarting");
                                        Multithreading.runAsync(() -> {
                                            String cmd = Hyperium.INSTANCE.getLaunchCommand(true);
                                            String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
                                            String iCmd = "\"" + java + "\" -jar \"" + new File(tmp, dl.getFileName()).getAbsolutePath() + "\" fw " + cmd;
                                            System.out.println("[Update Tab] Restart cmd: " + cmd);
                                            System.out.println("[Update Tab] Installer cmd: " + iCmd);
                                            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                                                try {
                                                    Runtime.getRuntime().exec(iCmd);
                                                    System.out.println("[Update Tab] Restarting...");
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }));
                                            Minecraft.getMinecraft().shutdown();
                                        });
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        installState = I18n.format("tab.update.installer.state.manualupdate");
                                    }
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
                gui.getTitle().drawCenteredString(I18n.format("tab.update.update"), x + 85, y + height - 45, 0xffffff);
            else {
                gui.getTitle().drawCenteredString(dl.getProgress() == 100 ? I18n.format("tab.update.updating") : I18n.format("tab.update.downloading"), x + 85, y + height - 45, 0x969696);
                gui.getFont().drawCenteredString(dl.getProgress() == 100 ? installState : dl.getProgress() + "%", x + 85, y + height - 30, 0x969696);
                if (dl.getProgress() != 100) {
                    Gui.drawRect(0, gui.height - 2, gui.width / 100 * dl.getProgress(), gui.height, 0xff95c990);
                }
            }
        }
    }

    public boolean isBusy() {
        return dl != null;
    }
}
