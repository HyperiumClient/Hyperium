package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.installer.InstallerFrame;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.Downloader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AddonsInstallerTab extends AbstractTab {
    private static final char[] hexCodes = "0123456789ABCDEF".toCharArray();
    private static int offsetY = 0; // static so it saves the previous location
    public File addonsDir = new File(Minecraft.getMinecraft().mcDataDir, "addons");
    public int current;
    public String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";
    private int y, w;
    private GuiBlock block;

    public AddonsInstallerTab(int y, int w) {
        Multithreading.runAsync(() -> {
            this.y = y;
            this.w = w;
            int yi = 0, xi = 0, current = 0;
            List<JSONObject> ao = new ArrayList<>();
            JSONObject versionsJson = null;
            try {
                versionsJson = new JSONObject(InstallerFrame.get(versions_url));
            } catch (IOException e) {
                e.printStackTrace();
            }

            block = new GuiBlock(0, w, y, y + w);

            for (Object o : versionsJson.getJSONArray("addons")) {
                ao.add((JSONObject) o);
                int Current = current;
                items.add(new SettingItem(() -> {
                    try {
                        installAddon(ao.get(Current).getString("name"));
                    } catch (IOException e) {
                        HyperiumMainGui.INSTANCE.getAlerts().add(new HyperiumMainGui.Alert(Icons.ERROR.getResource(), null, "Failed to download Addon: " + ao.get(Current).getString("name")));
                        e.printStackTrace();
                    }
                }, Icons.DOWNLOAD.getResource(), ao.get(current).getString("name"), ao.get(current).getString("description"), "Download Addon", xi, yi));

                if (xi == 2) {
                    xi = 0;
                    yi++;
                } else
                    xi++;
                current++;
            }
        });
    }

    private static String toHex(byte[] bytes) {
        StringBuilder r = new StringBuilder(bytes.length * 2);

        for (byte b : bytes) {
            r.append(hexCodes[(b >> 4) & 0xF]);
            r.append(hexCodes[(b & 0xF)]);
        }
        return r.toString();
    }

    @Override
    public void drawTabIcon() {
        Icons.DOWNLOAD.bind();
        Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight(float s) {
        Gui.drawRect(0, (int) (y + s * (s * w / 2)), 3, (int) (y + w - s * (w / 2)), Color.WHITE.getRGB());
    }

    private void installAddon(String jsonName) throws IOException {
        JSONObject versionsJson = new JSONObject(InstallerFrame.get("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json"));
        JSONArray addonsArray = versionsJson.getJSONArray("addons");
        List<JSONObject> addonObjects = new ArrayList<>();
        for (Object o : addonsArray)
            addonObjects.add((JSONObject) o);
        AtomicReference<JSONObject> addon = new AtomicReference<>(addonObjects.stream().filter(o -> o.getString("name").equals(jsonName)).findFirst().get());
        File addonsDir = new File(Minecraft.getMinecraft().mcDataDir, "pending-addons");
        addonsDir.mkdir();
        File aOut = new File(addonsDir, addon.get().getString("name") + "-" + addon.get().getString("version") + ".jar");
        downloadFile(new URL(addon.get().getString("url")), aOut, addon.get().getString("name"));
    }

    /**
     * Downloads a file.
     *
     * @param url    the URL of the file to download
     * @param output the file to output
     * @throws IOException when downloading fails
     */
    private void downloadFile(URL url, File output, String name) throws IOException {
        JSONObject versionsJson = new JSONObject(InstallerFrame.get("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json"));
        JSONArray addonsArray = versionsJson.getJSONArray("addons");
        List<JSONObject> addonObjects = new ArrayList<>();
        for (Object o : addonsArray)
            addonObjects.add((JSONObject) o);
        AtomicReference<JSONObject> addon = new AtomicReference<>();

        if (!output.getParentFile().exists()) {
            output.getParentFile().mkdirs();
            if (output.exists()) {
                Downloader downloader = new Downloader();
                downloader.download(url, output);
                System.out.println("Downloading: " + addon.get().getString("url"));
                File aOut = new File(addonsDir, addon.get().getString("name") + "-" + addon.get().getString("version") + ".jar");
                if (!toHex(checksum(aOut, "SHA-256")).equalsIgnoreCase(addon.get().getString("sha256"))) {
                    HyperiumMainGui.INSTANCE.getAlerts().add(new HyperiumMainGui.Alert(Icons.ERROR.getResource(), null, "SHA256 does not match"));
                }
            } else {
                HyperiumMainGui.Alert alert = new HyperiumMainGui.Alert(Icons.EXTENSION.getResource(), () -> {
                }, "You already have " + name + " installed!");
                HyperiumMainGui.INSTANCE.getAlerts().add(alert);
            }
        }
    }

    private byte[] checksum(File input, String name) {
        try (InputStream in = new FileInputStream(input)) {
            MessageDigest digest = MessageDigest.getInstance(name);
            byte[] block = new byte[4096];
            int length;
            while ((length = in.read(block)) > 0)
                digest.update(block, 0, length);
            return digest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}