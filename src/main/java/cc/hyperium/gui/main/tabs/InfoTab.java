package cc.hyperium.gui.main.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.GuiHyperiumCredits;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.OverlayButton;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.installer.InstallerConfig;
import cc.hyperium.installer.utils.DownloadTask;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UpdateUtils;
import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

/*
 * Created by Cubxity on 23/05/2018
 */
public class InfoTab extends AbstractTab {
    private static HyperiumOverlay licenses = new HyperiumOverlay();
    private GuiBlock block;
    private int y, w;

    static {
        Multithreading.runAsync(() -> {
            try {
                HttpGet get = new HttpGet("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/opensource.json");
                JsonHolder json = new JsonHolder(IOUtils.toString(HomeTab.hc.execute(get).getEntity().getContent(), Charsets.UTF_8));
                for (String key : json.getKeys()) {
                    licenses.getComponents().add(new OverlayButton(key, () -> {
                        try {
                            Desktop.getDesktop().browse(new URL(json.optString(key)).toURI());
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public InfoTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;

        //TODO: Add buttons to view license in the licenses overlay

        items.add(new SettingItem(() -> new GuiHyperiumCredits(HyperiumMainGui.INSTANCE).show(), null, "Credits", "List of contributors who helped with the client", "Lists of contributor name and amount of commits", 2, 0));
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(licenses), null, "Licenses", "Open Source Licenses", "Licenses for open source libraries used in the client", 2, 1));
        items.add(new SettingItem(() -> Multithreading.runAsync(() -> {
            if (!UpdateUtils.INSTANCE.isSupported() ) {
                try {
                    UpdateUtils utils = UpdateUtils.INSTANCE;
                    Hyperium.INSTANCE.getNotification().display("Update", "Downloading updates...", 3);
                    JsonObject ver = StreamSupport.stream(utils.vJson.optJSONArray("versions").spliterator(), false)
                            .filter(v -> utils.vJson.optString("latest-stable").equals(v.getAsJsonObject().get("name").getAsString())).findFirst()
                            .orElseThrow(() -> new IllegalStateException("Couldn't find stable version")).getAsJsonObject();
                    boolean download = ver.get("install-min").getAsInt() > InstallerConfig.VERSION;
                    System.out.println("Download=" + download);
                    Multithreading.runAsync(() -> {
                        try {
                            File jar;
                            if (download || Hyperium.INSTANCE.isDevEnv()) { // or else it will break in dev env
                                DownloadTask task = new DownloadTask(ver.get("url").getAsString(), System.getProperty("java.io.tmpdir"));
                                task.execute();
                                task.get();
                                jar = new File(System.getProperty("java.io.tmpdir"), task.getFileName());
                            } else {
                                jar = new File(System.getProperty("sun.java.command").split(" ")[0]);
                            }
                            HyperiumMainGui.getAlerts().add(new HyperiumMainGui.Alert(Icons.ERROR.getResource(), () -> {
                            }, "Client will restart in 10 seconds to update"));
                            Multithreading.schedule(() -> {
                                Minecraft.getMinecraft().shutdown();
                                try {
                                    Runtime.getRuntime().exec(System.getProperty("java.home") + "/bin/java -jar " + jar.getAbsolutePath());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }, 10, TimeUnit.SECONDS);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            HyperiumMainGui.getAlerts().add(new HyperiumMainGui.Alert(Icons.ERROR.getResource(), () -> {
                            }, "Update - Failed to download updates"));
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    HyperiumMainGui.getAlerts().add(new HyperiumMainGui.Alert(Icons.ERROR.getResource(), () -> {
                    }, "Update - Failed to download updates"));
                }
            } else
                HyperiumMainGui.getAlerts().add(new HyperiumMainGui.Alert(Icons.TOOL.getResource(), () -> {
                }, "Client is up to date!"));

        }), null, "Update", "update the client", "click to update the client if updates are available", 2, 2));
    }

    @Override
    public void drawTabIcon() {
        Icons.INFO.bind();
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

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        super.draw(mouseX, mouseY, topX, topY, containerWidth, containerHeight);
    }

    @Override
    public boolean isScrollable() {
        return false;
    }
}
