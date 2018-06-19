package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.installer.InstallerFrame;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.Downloader;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.json.JSONObject;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.StreamSupport;

public class AddonsInstallerTab extends AbstractTab {
    private static final char[] hexCodes = "0123456789ABCDEF".toCharArray();
    public String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";
    HashMap<Object, Boolean> clicked = new HashMap<>();
    private int y, w;
    private GuiBlock block;
    private HashMap<Object, SettingItem> items1 = new HashMap<>();

    public AddonsInstallerTab(int y, int w) {
        Multithreading.runAsync(() -> {
            this.y = y;
            this.w = w;
            int yi = 0, xi = 0, current = 0;
            List<JSONObject> ao = new ArrayList<>();
            JSONObject versionsJson;
            try {
                versionsJson = new JSONObject(InstallerFrame.get(versions_url));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            block = new GuiBlock(0, w, y, y + w);

            for (Object o : versionsJson.getJSONArray("addons")) {
                ao.add((JSONObject) o);
                int finalCurrent = current;
                //I know this is hacky nad a memory leak but eh
                SettingItem e1 = new SettingItem(() -> Multithreading.runAsync(() -> {
                    if (clicked.containsKey(o))
                        return;
                    clicked.put(o, true);
                    try {
                        installAddon(ao.get(finalCurrent).getString("name"));
                        SettingItem settingItem = items1.get(o);
                        settingItem.setDesc(settingItem.getDesc() + " \n Downloaded. Restart your game to apply change.");
                    } catch (IOException e) {
                        HyperiumMainGui.INSTANCE.getAlerts().add(new HyperiumMainGui.Alert(Icons.ERROR.getResource(), null, "Failed to download " + ao.get(finalCurrent).getString("name")));
                        e.printStackTrace();
                    }
                }), Icons.DOWNLOAD.getResource(), ao.get(current).getString("name"), ao.get(current).getString("description"), "Download Addon", xi, yi);
                items1.put(o, e1);
                items.add(e1);

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
        JsonArray addons = new JsonHolder(InstallerFrame.get("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json")).optJSONArray("addons");
        JsonHolder addon = new JsonHolder(StreamSupport.stream(addons.spliterator(), false).filter(o -> o.getAsJsonObject().get("name").getAsString().equals(jsonName)).findFirst().get().getAsJsonObject());
        File addonsDir = new File(Minecraft.getMinecraft().mcDataDir, "pending-addons");
        addonsDir.mkdir();
        File aOut = new File(addonsDir, addon.optString("name") + "-" + addon.optString("version") + ".jar");
        downloadFile(aOut, addon);
    }

    /**
     * Downloads the addon
     *
     * @param output the file to output
     */
    private void downloadFile(File output, JsonHolder addon) throws MalformedURLException {
        if (output.exists())
            HyperiumMainGui.INSTANCE.getAlerts().add(new HyperiumMainGui.Alert(Icons.EXTENSION.getResource(), () -> {
            }, "You already have " + addon.optString("name") + " installed and up to date"));
        else {
            Downloader downloader = new Downloader();
            System.out.println("Downloading: " + addon.optString("url"));
            downloader.download(new URL(addon.optString("url")), output);
            if (!toHex(checksum(output, "SHA-256")).equalsIgnoreCase(addon.optString("sha256"))) {
                HyperiumMainGui.INSTANCE.getAlerts().add(new HyperiumMainGui.Alert(Icons.ERROR.getResource(), null, "SHA256 does not match"));
                output.delete(); // we don't want user to get unverified file
            } else
                HyperiumMainGui.INSTANCE.getAlerts().add(new HyperiumMainGui.Alert(Icons.EXTENSION.getResource(), null, "Addon was successfully installed"));
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