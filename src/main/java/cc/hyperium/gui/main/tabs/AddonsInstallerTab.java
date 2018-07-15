package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.installer.api.entities.AddonManifest;
import cc.hyperium.installer.api.entities.InstallerManifest;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.Downloader;
import cc.hyperium.utils.InstallerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddonsInstallerTab extends AbstractTab {
    private int y, w;
    private GuiBlock block;

    public AddonsInstallerTab(int y, int w) {
        Map<AddonManifest, SettingItem> items = new HashMap<>();
        Multithreading.runAsync(() -> {
            this.y = y;
            this.w = w;
            int yi = 0, xi = 0, current = 0;
            InstallerManifest manifest = InstallerUtils.getManifest();

            block = new GuiBlock(0, w, y, y + w);

            for (AddonManifest addon : manifest.getAddons()) {
                int finalCurrent = current;
                //I know this is hacky nad a memory leak but eh
                SettingItem e1 = new SettingItem(() -> Multithreading.runAsync(() -> {
                    try {
                        installAddon(addon.getName());
                        SettingItem settingItem = items.get(addon);
                        settingItem.setDesc(settingItem.getDesc() + " \n Downloaded. Restart your game to apply change.");
                    } catch (IOException e) {
                        HyperiumMainGui.INSTANCE.getAlerts().add(new HyperiumMainGui.Alert(Icons.ERROR.getResource(), null, "Failed to download " + addon.getName()));
                        e.printStackTrace();
                    }
                }), Icons.DOWNLOAD.getResource(), addon.getName(), addon.getDescription(), "Download Addon", xi, yi);
                items.put(addon, e1);

                if (xi == 2) {
                    xi = 0;
                    yi++;
                } else
                    xi++;
                current++;
            }
        });
    }

    @Override
    public void drawTabIcon() {
        if (HyperiumMainGui.INSTANCE.getCurrentTab() instanceof AddonsInstallerTab) {
            Icons.DOWNLOAD.bind();
            Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
        }
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight(float s) {
        if (HyperiumMainGui.INSTANCE.getCurrentTab().equals(this))
            Gui.drawRect(0, (int) (y + s * (s * w / 2)), 3, (int) (y + w - s * (w / 2)), Color.WHITE.getRGB());
    }

    private void installAddon(String jsonName) throws IOException {
        AddonManifest addon = Arrays.stream(InstallerUtils.getManifest().getAddons()).filter(o -> o.getName().equals(jsonName)).findFirst().get();
        File addonsDir = new File(Minecraft.getMinecraft().mcDataDir, "pending-addons");
        addonsDir.mkdir();
        File aOut = new File(addonsDir, addon.getName() + "-" + addon.getVersion() + ".jar");
        downloadFile(aOut, addon);
    }

    /**
     * Downloads the addon
     *
     * @param output the file to output
     * @param addon
     */
    private void downloadFile(File output, AddonManifest addon) throws MalformedURLException {
        if (output.exists())
            HyperiumMainGui.INSTANCE.getAlerts().add(new HyperiumMainGui.Alert(Icons.EXTENSION.getResource(), () -> {
            }, "You already have " + addon.getName() + " installed and up to date"));
        else {
            Downloader downloader = new Downloader();
            System.out.println("Downloading: " + addon.getUrl());
            downloader.download(new URL(addon.getUrl()), output);
            if (!InstallerUtils.toHex(InstallerUtils.checksum(output, "SHA-256")).equalsIgnoreCase(addon.getSha256())) {
                HyperiumMainGui.INSTANCE.getAlerts().add(new HyperiumMainGui.Alert(Icons.ERROR.getResource(), null, "SHA256 does not match"));
                output.delete(); // we don't want user to get unverified file
            } else
                HyperiumMainGui.INSTANCE.getAlerts().add(new HyperiumMainGui.Alert(Icons.EXTENSION.getResource(), null, "Addon was successfully installed"));
        }
    }
}